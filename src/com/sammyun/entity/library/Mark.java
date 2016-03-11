package com.sammyun.entity.library;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sammyun.entity.Admin;
import com.sammyun.entity.BaseEntity;

/**
 * 书得分
 * 
 * @author maxu
 */
@Entity
@Table(name = "t_pe_mark")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "t_pe_mark_sequence")
public class Mark extends BaseEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -2798028156336348301L;

    /**
     * 书
     */
    private Book book;

    /**
     * 用户
     */
    private Admin admin;

    /**
     * 得分
     */
    private int mark;

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

    public Admin getAdmin()
    {
        return admin;
    }

    public void setAdmin(Admin admin)
    {
        this.admin = admin;
    }

    public int getMark()
    {
        return mark;
    }

    public void setMark(int mark)
    {
        this.mark = mark;
    }

}
