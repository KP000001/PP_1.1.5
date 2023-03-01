package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS table_user" +
                    "(id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    " name VARCHAR(100), " + " lastName VARCHAR(100), " +
                    " age INTEGER)").executeUpdate();
            transaction.commit();
            System.out.println("Таблица создана");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS table_user").executeUpdate();
            transaction.commit();
            System.out.println("Таблица удалена");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            NativeQuery user = session.createSQLQuery("INSERT INTO table_user(name,lastName,age) VALUES (?,?,?)");
            user.setParameter(1, name);
            user.setParameter(2, lastName);
            user.setParameter(3, age);
            user.executeUpdate();
            transaction.commit();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            NativeQuery userId = session.createSQLQuery("DELETE FROM table_user WHERE id = ?");
            userId.setParameter(1, id);
            userId.executeUpdate();
            transaction.commit();
            System.out.println("User " + id + " удален");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            NativeQuery users = session.createSQLQuery("SELECT * FROM table_user");
            List<Object[]> us = users.list();
            for (Object[] u : us) {
                User user = new User();
                user.setId(Long.parseLong(u[0].toString()));
                user.setName(u[1].toString());
                user.setLastName(u[2].toString());
                user.setAge(Byte.parseByte(u[3].toString()));
                allUsers.add(user);
            }
            for (User value : allUsers) {
                System.out.println(value.toString());
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE User").executeUpdate();
            transaction.commit();
            System.out.println("Таблица очищена");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
                System.out.println("Таблица не очищена");
            }
        }
    }
}
