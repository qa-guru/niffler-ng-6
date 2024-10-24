package guru.qa.niffler.data.jpa;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;

public class ThreadSafeEntityManager implements EntityManager {

  private final ThreadLocal<EntityManager> threadEm = new ThreadLocal<>();
  private final EntityManagerFactory emf;

  public ThreadSafeEntityManager(EntityManager delegate) {
    threadEm.set(delegate);
    emf = delegate.getEntityManagerFactory();
  }

  private EntityManager threadEm() {
    if (threadEm.get() == null || !threadEm.get().isOpen()) {
      threadEm.set(emf.createEntityManager());
    }
    return threadEm.get();
  }

  @Override
  public void close() {
    if (threadEm.get() != null && threadEm.get().isOpen()) {
      threadEm.get().close();
      threadEm.remove();
    }
  }

  @Override
  public void persist(Object entity) {
    threadEm().persist(entity);
  }

  @Override
  public <T> T merge(T entity) {
    return threadEm().merge(entity);
  }

  @Override
  public void remove(Object entity) {
    threadEm().remove(entity);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey) {
    return threadEm().find(entityClass, primaryKey);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
    return threadEm().find(entityClass, primaryKey, properties);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
    return threadEm().find(entityClass, primaryKey, lockMode);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
    return threadEm().find(entityClass, primaryKey, lockMode, properties);
  }

  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey) {
    return threadEm().getReference(entityClass, primaryKey);
  }

  @Override
  public void flush() {
    threadEm().flush();
  }

  @Override
  public void setFlushMode(FlushModeType flushMode) {
    threadEm().setFlushMode(flushMode);
  }

  @Override
  public FlushModeType getFlushMode() {
    return threadEm().getFlushMode();
  }

  @Override
  public void lock(Object entity, LockModeType lockMode) {
    threadEm().lock(entity, lockMode);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    threadEm().lock(entity, lockMode, properties);
  }

  @Override
  public void refresh(Object entity) {
    threadEm().refresh(entity);
  }

  @Override
  public void refresh(Object entity, Map<String, Object> properties) {
    threadEm().refresh(entity, properties);
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode) {
    threadEm().refresh(entity, lockMode);
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    threadEm().refresh(entity, lockMode, properties);
  }

  @Override
  public void clear() {
    threadEm().clear();
  }

  @Override
  public void detach(Object entity) {
    threadEm().detach(entity);
  }

  @Override
  public boolean contains(Object entity) {
    return threadEm().contains(entity);
  }

  @Override
  public LockModeType getLockMode(Object entity) {
    return threadEm().getLockMode(entity);
  }

  @Override
  public void setProperty(String propertyName, Object value) {
    threadEm().setProperty(propertyName, value);
  }

  @Override
  public Map<String, Object> getProperties() {
    return threadEm().getProperties();
  }

  @Override
  public Query createQuery(String qlString) {
    return threadEm().createQuery(qlString);
  }

  @Override
  public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
    return threadEm().createQuery(criteriaQuery);
  }

  @Override
  public Query createQuery(CriteriaUpdate updateQuery) {
    return threadEm().createQuery(updateQuery);
  }

  @Override
  public Query createQuery(CriteriaDelete deleteQuery) {
    return threadEm().createQuery(deleteQuery);
  }

  @Override
  public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
    return threadEm().createQuery(qlString, resultClass);
  }

  @Override
  public Query createNamedQuery(String name) {
    return threadEm().createNamedQuery(name);
  }

  @Override
  public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
    return threadEm().createNamedQuery(name, resultClass);
  }

  @Override
  public Query createNativeQuery(String sqlString) {
    return threadEm().createNativeQuery(sqlString);
  }

  @Override
  public Query createNativeQuery(String sqlString, Class resultClass) {
    return threadEm().createNativeQuery(sqlString, resultClass);
  }

  @Override
  public Query createNativeQuery(String sqlString, String resultSetMapping) {
    return threadEm().createNativeQuery(sqlString, resultSetMapping);
  }

  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
    return threadEm().createNamedStoredProcedureQuery(name);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
    return threadEm().createStoredProcedureQuery(procedureName);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
    return threadEm().createStoredProcedureQuery(procedureName, resultClasses);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
    return threadEm().createStoredProcedureQuery(procedureName, resultSetMappings);
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
  public <T> T unwrap(Class<T> cls) {
    return threadEm().unwrap(cls);
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
  public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
    return threadEm().createEntityGraph(rootType);
  }

  @Override
  public EntityGraph<?> createEntityGraph(String graphName) {
    return threadEm().createEntityGraph(graphName);
  }

  @Override
  public EntityGraph<?> getEntityGraph(String graphName) {
    return threadEm().getEntityGraph(graphName);
  }

  @Override
  public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
    return threadEm().getEntityGraphs(entityClass);
  }
}
