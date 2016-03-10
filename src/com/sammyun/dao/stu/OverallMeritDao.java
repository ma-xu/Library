package com.sammyun.dao.stu;

import java.util.List;

import com.sammyun.Page;
import com.sammyun.Pageable;
import com.sammyun.dao.BaseDao;
import com.sammyun.entity.dict.DictSchool;
import com.sammyun.entity.dict.DictStudent;
import com.sammyun.entity.stu.OverallMerit;

/**
 * OverallMerit * Dao - 综合评价
 * 
 * @author Sencloud Team

 */
public interface OverallMeritDao extends BaseDao<OverallMerit, Long> 
{
    /**
     * 根据学校查询相关综合评价
     * <功能详细描述>
     * @param dictSchool
     * @param orders
     * @return
     * @see [类、类#方法、类#成员]
     */
      public Page<OverallMerit> findBySchool(DictSchool dictSchool,Pageable pageable);
      
      /**
       * 根据学生查询相关综合评价
       * <功能详细描述>
       * @param dictSchool
       * @param pageable
       * @return
       * @see [类、类#方法、类#成员]
       */
       public List<OverallMerit> findByDictStudent(DictStudent dictStudent);
       
       /**
        * 根据学生删除其对应的所有作品
        * @param dictStudent
        * @see [类、类#方法、类#成员]
        */
       public void deleteByDictStudent(DictStudent dictStudent);
}
