package guru.qa.niffler.data.jpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;

public class ThreadSafeEntityManager implements EntityManager {

    private final ThreadLocal<EntityManager> threadEn = new ThreadLocal<>();
    private final EntityManagerFactory emf;

    public ThreadSafeEntityManager(EntityManager delegate) {
        threadEn.set(delegate);
        emf = delegate.getEntityManagerFactory();
    }

    private EntityManager threadEm() {
        if(threadEn.get() == null || !threadEn.get().isOpen()){
            threadEn.set(emf.createEntityManager());
        }
        return threadEn.get();
    }

    @Override
    public void close() {
        if(threadEn.get() == null && threadEn.get().isOpen()){
            threadEn.get().close();
            threadEn.remove();
        }
    }

    @Override
    public void persist(Object o) {
        threadEm().persist(o);
    }

    @Override
    public <T> T merge(T t) {
        return threadEm().merge(t);
    }

    @Override
    public void remove(Object o) {
        threadEm().remove(o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return threadEm().find(aClass, o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return threadEm().find(aClass, o, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return threadEm().find(aClass, o, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        return threadEm().find(aClass, o, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return threadEm().getReference(aClass, o);
    }

    @Override
    public void flush() {
        threadEm().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        threadEm().setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return threadEm().getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        threadEm().lock(o, lockModeType);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        threadEm().lock(o, lockModeType, map);
    }

    @Override
    public void refresh(Object o) {
        threadEm().refresh(o);
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        threadEm().refresh(o, map);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        threadEm().refresh(o, lockModeType);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        threadEm().refresh(o, lockModeType, map);
    }

    @Override
    public void clear() {
        threadEm().clear();
    }

    @Override
    public void detach(Object o) {
        threadEm().detach(o);
    }

    @Override
    public boolean contains(Object o) {
        return threadEm().contains(o);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return threadEm().getLockMode(o);
    }

    @Override
    public void setProperty(String s, Object o) {
        threadEm().setProperty(s, o);
    }

    @Override
    public Map<String, Object> getProperties() {
        return threadEm().getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return threadEm().createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return threadEm().createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return threadEm().createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return threadEm().createQuery(criteriaDelete);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return threadEm().createQuery(s, aClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return threadEm().createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return threadEm().createNamedQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return threadEm().createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return threadEm().createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return threadEm().createNativeQuery(s, s1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return threadEm().createNamedStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return threadEm().createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return threadEm().createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return threadEm().createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        threadEm().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return threadEm().isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return threadEm().unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return threadEm().getDelegate();
    }

    @Override
    public boolean isOpen() {
        return threadEm().isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return threadEm().getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return threadEm().getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return threadEm().getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return threadEm().getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return threadEm().createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return threadEm().createEntityGraph(s);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return threadEm().getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return threadEm().getEntityGraphs(aClass);
    }
}
