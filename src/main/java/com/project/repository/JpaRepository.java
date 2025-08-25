package com.project.repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public abstract class JpaRepository<T, ID> implements GenericRepository<T, ID> {

    protected final EntityManagerFactory emf;
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public JpaRepository(EntityManagerFactory emf) {
        this.emf = emf;
        // Sử dụng reflection để lấy kiểu T lúc runtime
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T save(T entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Nếu entity đã tồn tại (managed), merge sẽ cập nhật.
            // Nếu chưa, nó sẽ tạo mới.
            T mergedEntity = em.merge(entity);
            tx.commit();
            return mergedEntity;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Không thể lưu entity", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        EntityManager em = emf.createEntityManager();
        try {
            // T.class lấy từ constructor
            return Optional.ofNullable(em.find(entityClass, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            // Tạo câu lệnh truy vấn JPQL
            return em.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
        } finally {
            em.close();
        }
    }


    @Override
    public void delete(T entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Để xóa một entity, nó cần phải ở trạng thái managed
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Không thể xóa entity", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }
}