package com.zaqacu.dao;

import com.zaqacu.entity.EmployStatus;

public interface EmployStatusMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employ_status
     *
     * @mbggenerated Tue Aug 20 20:18:17 CST 2019
     */
    int deleteByPrimaryKey(Integer employStatusId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employ_status
     *
     * @mbggenerated Tue Aug 20 20:18:17 CST 2019
     */
    int insert(EmployStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employ_status
     *
     * @mbggenerated Tue Aug 20 20:18:17 CST 2019
     */
    int insertSelective(EmployStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employ_status
     *
     * @mbggenerated Tue Aug 20 20:18:17 CST 2019
     */
    EmployStatus selectByPrimaryKey(Integer employStatusId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employ_status
     *
     * @mbggenerated Tue Aug 20 20:18:17 CST 2019
     */
    int updateByPrimaryKeySelective(EmployStatus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employ_status
     *
     * @mbggenerated Tue Aug 20 20:18:17 CST 2019
     */
    int updateByPrimaryKey(EmployStatus record);
}