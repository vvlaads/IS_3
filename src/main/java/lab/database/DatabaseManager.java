package lab.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lab.data.Movie;
import lab.data.Person;
import lab.util.DBObject;
import lab.util.Validator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.ValidationException;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

@ApplicationScoped
public class DatabaseManager {
    private EntityManagerFactory emf;

    @Inject
    private DBCPDataSourceProvider provider;

    private EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    @PostConstruct
    void init() {
        Map<String, Object> props = new HashMap<>();
        props.put("javax.persistence.nonJtaDataSource", provider.getDataSource());
        emf = Persistence.createEntityManagerFactory("PersistenceUnit", props);
    }

    @PreDestroy
    void shutdown() {
        if (emf != null) {
            emf.close();
        }
    }

    public void addObject(DBObject object) {
        EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            if (!Validator.isValidObject(object)) {
                throw new IllegalArgumentException(object.getClass() + " validation failed");
            }

            em.persist(object);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.err.println(e.getMessage());
        }
    }

    public void updateObject(DBObject object) {
        EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            if (Validator.isValidObject(object)) {
                if (em.find(object.getClass(), object.getId()) == null) {
                    throw new RuntimeException(object.getClass() + " doesn't exist");
                }
                em.merge(object);
            } else {
                throw new IllegalArgumentException(object.getClass() + "validation failed");
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.err.println(e.getMessage());
        }
    }

    public <T extends DBObject> void deleteObject(Class<T> entityClass, int id) {
        EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            DBObject existObject = em.find(entityClass, id);
            if (existObject == null) {
                throw new RuntimeException(entityClass + " with id: " + id + " doesn't exist");
            }
            em.remove(existObject);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.err.println(e.getMessage());
        }
    }

    public <T extends DBObject> List<T> getObjectList(Class<T> clazz) {
        EntityManager em = createEntityManager();
        String query = "SELECT obj FROM " + clazz.getSimpleName() + " obj";
        return em.createQuery(query, clazz).getResultList();
    }

    public <T extends DBObject> T getObjectById(Class<T> clazz, int id) {
        EntityManager em = createEntityManager();
        try {
            T obj = em.find(clazz, id);
            if (obj == null) {
                System.err.println(clazz.getSimpleName() + " not found for id: " + id);
            }
            return obj;
        } catch (Exception e) {
            System.err.println("Error while fetching " + clazz.getSimpleName() + " by id: " + id);
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Integer deleteMovieByGoldenPalmCount(int count) {
        EntityManager em = createEntityManager();
        try {
            return (Integer) em.createNativeQuery("SELECT delete_movie_by_golden_palm_count(?)")
                    .setParameter(1, count)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public List<Movie> getMoviesByNamePrefix(String prefix) {
        EntityManager em = createEntityManager();
        try {
            return em.createNativeQuery(
                            "SELECT * FROM get_movies_by_name_prefix(?)", Movie.class)
                    .setParameter(1, prefix)
                    .getResultList();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }


    public List<Movie> findMoviesByGoldenPalmCountGreaterThan(int minCount) {
        EntityManager em = createEntityManager();
        try {
            return em.createNativeQuery("SELECT * FROM get_movies_by_golden_palm_count(?)", Movie.class)
                    .setParameter(1, minCount)
                    .getResultList();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }


    public List<Person> findOperatorsWithoutOscars() {
        EntityManager em = createEntityManager();
        try {
            List<Person> operators = em.createNativeQuery(
                            "SELECT * FROM get_operators_without_oscars()", Person.class)
                    .getResultList();

            System.out.println("Найдено операторов без фильмов-обладателей Оскара: " + operators.size());
            return operators;

        } catch (Exception e) {
            System.err.println("Ошибка при поиске операторов без оскаров: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void rewardLongMovies(int minLength, int oscarsToAdd) {
        EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createNativeQuery("SELECT reward_long_movies(?, ?)")
                    .setParameter(1, minLength)
                    .setParameter(2, oscarsToAdd)
                    .executeUpdate();
            transaction.commit();
            System.out.println("Наградили все фильмы длиннее " + minLength + " на " + oscarsToAdd + " Оскаров");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Ошибка при награждении фильмов: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }


    public List<Movie> findMoviesByCoordinatesId(int coordinatesId) {
        EntityManager em = createEntityManager();
        return em.createQuery("SELECT m FROM Movie m WHERE m.coordinates.id = :coordinatesId", Movie.class)
                .setParameter("coordinatesId", coordinatesId)
                .getResultList();
    }

    public List<Person> findPersonsByLocationId(int locationId) {
        EntityManager em = createEntityManager();
        return em.createQuery("SELECT p FROM Person p WHERE p.location.id = :locationId", Person.class)
                .setParameter("locationId", locationId)
                .getResultList();
    }

    public List<Movie> findMoviesByPersonId(int personId) {
        EntityManager em = createEntityManager();
        return em.createQuery("SELECT m FROM Movie m WHERE m.director.id = :personId " +
                        "OR m.screenwriter.id = :personId " +
                        "OR m.operator.id = :personId ", Movie.class)
                .setParameter("personId", personId)
                .getResultList();
    }

    public int importObjects(byte[] fileContent) {
        EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<DBObject> objects;
            JsonNode root = mapper.readTree(fileContent);
            if (root.isArray()) {
                objects = mapper.readValue(fileContent, new TypeReference<List<DBObject>>() {
                });
            } else if (root.isObject()) {
                DBObject obj = mapper.treeToValue(root, DBObject.class);
                objects = Collections.singletonList(obj);
            } else {
                throw new IOException("Unsupported JSON root type: " + root.getNodeType());
            }

            int totalCount = 0;
            for (DBObject obj : objects) {
                if (!Validator.isValidObject(obj)) {
                    throw new ValidationException("Ошибка валидации");
                }
                totalCount += countAllIncludesObjects(obj);
            }

            for (DBObject obj : objects) {
                em.persist(obj);
            }
            transaction.commit();
            return totalCount;
        } catch (Exception e) {
            System.err.println("Ошибка добавления: " + e.getMessage());
            transaction.rollback();
            return -1;
        }
    }

    private int countAllIncludesObjects(DBObject mainObject) {
        int count = 0;

        if (mainObject == null) {
            return 0;
        }

        count++;

        for (Field field : mainObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(mainObject);

                if (value instanceof DBObject) {
                    DBObject dbObject = (DBObject) value;
                    count += countAllIncludesObjects(dbObject);
                }
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
            }
        }
        return count;
    }
}
