package com.example.activiti.chapter7.service;

import com.example.activiti.chapter7.dao.LeaveDao;
import com.example.activiti.chapter7.entity.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-17
 * @Time: 16:06
 */
@Service
@Transactional
public class LeaveManager {

    @Autowired
    LeaveDao leaveDao;

    public void save(Leave leave) {
        leaveDao.save(leave);
    }

    public void delete(Long id) {
        leaveDao.delete(id);
    }

    @Transactional(readOnly = true)
    public void get(Long id) {
        leaveDao.get(id);

    }
}
