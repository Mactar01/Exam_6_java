package essai.org.dao;

import essai.org.model.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class MemberDAO {
    
    private final SessionFactory sessionFactory;
    
    public MemberDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public Member save(Member member) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(member);
            transaction.commit();
            return member;
        }
    }
    
    public Optional<Member> findByPseudo(String pseudo) {
        try (Session session = sessionFactory.openSession()) {
            Query<Member> query = session.createQuery(
                "FROM Member m WHERE m.pseudo = :pseudo", Member.class);
            query.setParameter("pseudo", pseudo);
            return query.uniqueResultOptional();
        }
    }
    
    public List<Member> findAllOnline() {
        try (Session session = sessionFactory.openSession()) {
            Query<Member> query = session.createQuery(
                "FROM Member m WHERE m.isOnline = true AND m.isBanned = false", Member.class);
            return query.list();
        }
    }
    
    public long countOnlineMembers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM Member m WHERE m.isOnline = true AND m.isBanned = false", Long.class);
            return query.uniqueResult();
        }
    }
    
    public void updateOnlineStatus(String pseudo, boolean isOnline) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery(
                "UPDATE Member m SET m.isOnline = :isOnline WHERE m.pseudo = :pseudo");
            query.setParameter("isOnline", isOnline);
            query.setParameter("pseudo", pseudo);
            query.executeUpdate();
            transaction.commit();
        }
    }
    
    public void banMember(String pseudo, String reason) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery(
                "UPDATE Member m SET m.isBanned = true, m.isOnline = false, m.banReason = :reason WHERE m.pseudo = :pseudo");
            query.setParameter("reason", reason);
            query.setParameter("pseudo", pseudo);
            query.executeUpdate();
            transaction.commit();
        }
    }
    
    public void deleteMember(String pseudo) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Member m WHERE m.pseudo = :pseudo");
            query.setParameter("pseudo", pseudo);
            query.executeUpdate();
            transaction.commit();
        }
    }
} 