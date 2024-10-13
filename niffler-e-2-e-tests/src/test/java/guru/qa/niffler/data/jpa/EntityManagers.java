package guru.qa.niffler.data.jpa;

import guru.qa.niffler.data.tpl.DataSources;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManagers {
    private EntityManagers() {
    }

    private static final Map<String, EntityManagerFactory> emfs = new ConcurrentHashMap<>();

    public static EntityManager em(String jdbcUrl) {
        return new ThreadSafeEntityManager(
                emfs.computeIfAbsent(
                        jdbcUrl,
                        key -> {
                            DataSources.dataSource(jdbcUrl);
                            return Persistence.createEntityManagerFactory(jdbcUrl);
                        }
                ).createEntityManager()
        );
    }
}