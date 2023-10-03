package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PersistenceMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try{
//            insertAndFind(em);
//            findAndFind(em);
//            guaranteeEquals(em);
//            buffer(em);
//            dirtyChecking(em);
//            flush(em);
//            detach(em);

            transaction.commit();
        } catch (Exception e){
            System.out.println("Exception e = " + e);
            transaction.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void detach(EntityManager em) {
        /**
         * select 쿼리만 나가고, update 쿼리는 나가지 않는다.
         * detach() 시 준영속 상태가 되기 때문에, 영속성 컨텍스에서 관리하지 않는다.
         * 준영속 상태로 만드는 법
         * 1. em.detach()
         * 2. em.clear()
         * 3. em.close()
         */
        Member findMember = em.find(Member.class, 150L);
        findMember.setName("AAAA");
        System.out.println("--------------");
//            em.detach(findMember);
        em.clear();
        Member findMember2 = em.find(Member.class, 150L);
    }

    private static void flush(EntityManager em) {
        /**
         * flush하면 모아둔 sql문을 바로 db에 보낸다.
         * flush하면 1차 캐시를 지우는 개념이 아니다.
         * 쓰기 지연 sql 저장소의 쿼리들을 한 번에 db에 반영되는 것이다.
         * flush는? 직접 호출하거나, tx commit 되거나, jpql 사용하면 flush 된다.
         */
        Member member = new Member(200L, "member200");
        em.persist(member);
        em.flush();
        System.out.println("----------------");
    }

    private static void dirtyChecking(EntityManager em) {
        /**
         * JPA는 변경 감지를 통해 DB에서 온 캐시와 변경된 사항이 있다면
         * update 쿼리를 날린다.
         */
        Member findMember = em.find(Member.class, 150L);
        findMember.setName("AAA");
    }

    private static void buffer(EntityManager em) {
        /**
         * 엔티티 등록, 트랜잭션을 지원하는 쓰기 지원
         * JPA는 커밋 시점에 SQL을 날린다.
         * JDBC BATCH size만큼 모았다가 네트워크 한 번 타고 모인 sql문만큼 보냄
         * 버퍼링 기능
         */
        Member memberA = new Member(150L, "A");
        Member memberB = new Member(151L, "B");

        em.persist(memberA);
        em.persist(memberB);
        System.out.println("---------------");
    }

    private static void guaranteeEquals(EntityManager em) {
        /**
         * 영속 엔티티의 동일성을 보장한다.
         */
        Member findMember1 = em.find(Member.class, 101L);
        Member findMember2 = em.find(Member.class, 101L);
        System.out.println("result = " + (findMember1 == findMember2));
    }

    private static void findAndFind(EntityManager em) {
        /**
         * 처음 찾을 때는 select 쿼리가 나간다.
         * id 101을 찾으면, 영속성 컨텍스트에 올린다(1차 캐시)
         * 다음부터 찾을 때는 select 쿼리가 나가지 않음
         */
        Member findMember1 = em.find(Member.class, 101L);
        Member findMember2 = em.find(Member.class, 101L);
    }

    private static void insertAndFind(EntityManager em) {
        //비영속
        Member member = new Member();
        member.setId(101L);
        member.setName("HelloJPA");

        //영속
        System.out.println("======BEFORE======");
        em.persist(member);
        System.out.println("======AFTER======");

        /**
         * SELECT 쿼리가 DB에 나가지 않는다. WHY? 1차 캐시에 저장되어 있기 때문에
         */
        Member findMember = em.find(Member.class, 101L);
        System.out.println("findMember = " + findMember.getId());
        System.out.println("findMember = " + findMember.getName());
    }
}
