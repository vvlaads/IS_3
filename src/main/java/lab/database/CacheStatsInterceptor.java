package lab.database;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.annotation.Priority;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.inject.Inject;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.IdentityMapAccessor;
import org.eclipse.persistence.sessions.server.ServerSession;

@Interceptor
@CacheStats
@Priority(Interceptor.Priority.APPLICATION)
public class CacheStatsInterceptor {

    private static EntityManagerFactory emf;

    private static boolean enabled = true;

    public static void enable() {
        enabled = true;
    }

    public static void disable() {
        enabled = false;
    }

    public static void setEntityManagerFactory(EntityManagerFactory emf) {
        CacheStatsInterceptor.emf = emf;
    }

    @AroundInvoke
    public Object logCacheStats(InvocationContext ctx) throws Exception {
        Object result = ctx.proceed();

        if (!enabled) {
            return result;
        }

        EntityManager em = emf.createEntityManager();
        try {
            JpaEntityManager jem = em.unwrap(JpaEntityManager.class);
            ServerSession session = jem.unwrap(ServerSession.class);

            IdentityMapAccessor ima = session.getIdentityMapAccessor();

            System.out.println("=== L2 CACHE STATE ===");
            System.out.println("=====================");
        } finally {
            em.close();
        }

        return result;
    }
}
