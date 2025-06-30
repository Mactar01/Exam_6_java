package essai.org.dao;

import essai.org.model.Message;
import essai.org.model.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class MessageDAO {
    
    private final SessionFactory sessionFactory;
    
    public MessageDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public Message save(Message message) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(message);
            transaction.commit();
            return message;
        }
    }
    
    public List<Message> getLastMessages(int limit) {
        try (Session session = sessionFactory.openSession()) {
            Query<Message> query = session.createQuery(
                "FROM Message m ORDER BY m.sendDate DESC", Message.class);
            query.setMaxResults(limit);
            return query.list();
        }
    }
    
    public List<Message> getAllMessages() {
        try (Session session = sessionFactory.openSession()) {
            Query<Message> query = session.createQuery(
                "FROM Message m ORDER BY m.sendDate ASC", Message.class);
            return query.list();
        }
    }
    
    public void deleteMessagesByMember(Member member) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Message m WHERE m.sender = :member");
            query.setParameter("member", member);
            query.executeUpdate();
            transaction.commit();
        }
    }
} 