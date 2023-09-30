package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
//        save(em, transaction, member);

//        find(em, transaction, 1L);

//        update(em, transaction, 1L, "HelloJpa");

        try{
//            Member findMember = em.find(Member.class, 1L);

            /**
             * JPQL을 객체를 대상으로 하는 객체지향 쿼리이며,
             * 방언에 맞춰 각 DB에 맞춰 번역해준다.
             * MySQL -> Oracle로 바꿔도 코드 변경 거의 없음
             */
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();
            for (Member member : result) {
                System.out.println("member = " + member.getName());
            }

            transaction.commit();
        } catch (Exception e){
            System.out.println("Exception e = " + e);
            transaction.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void update(EntityManager em, EntityTransaction transaction, Long id, String updatingName) {
        try{
            Member findMember = em.find(Member.class, id);
            /**
             * JPA를 통해 엔티티를 가져오면, 트랜잭션을 커밋하는 시점에
             * 변경을 감지해서 update 쿼리를 날린다.
             */
            findMember.setName(updatingName);

            transaction.commit();
        } catch (Exception e){
            System.out.println("Exception e = " + e);
            transaction.rollback();
        } finally {
            em.close();
        }
    }

    private static void find(EntityManager em, EntityTransaction transaction, Long id) {
        try{
            Member findMember = em.find(Member.class, id);
            System.out.println("findMember = " + findMember.getId());
            System.out.println("findMember = " + findMember.getName());

            transaction.commit();
        } catch (Exception e){
            System.out.println("Exception e = " + e);
            transaction.rollback();
        } finally {
            em.close();
        }
    }


    private static void save(EntityManager em, EntityTransaction transaction, Member member) {
        try{
            em.persist(member);

            transaction.commit();
        } catch (Exception e){
            System.out.println("Exception e = " + e);
            transaction.rollback();
        } finally {
            em.close();
        }
    }
}
