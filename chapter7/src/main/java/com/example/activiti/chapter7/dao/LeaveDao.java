package com.example.activiti.chapter7.dao;

import com.example.activiti.chapter7.entity.Leave;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-17
 * @Time: 15:55
 */
@Repository
public class LeaveDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Leave leave) {

        getSession().saveOrUpdate(leave);
    }

    public void delete(Long id) {
        getSession().delete(get(id));
    }


    public Leave get(Long id) {
        return (Leave) getSession().get(Leave.class, id);
    }

    /**
     * 获取当前的session
     * @return
     */
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
