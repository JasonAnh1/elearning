package com.jason.elearning.repository.user;


import com.jason.elearning.entity.QUser;
import com.jason.elearning.entity.User;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.jason.elearning.util.Util.PAGE_SIZE;


public class UserRepositoryImpl extends BaseRepository implements UserRepositoryCustom {


    @Override
    public List<User> getListUser(int page, String phone, String name, boolean deleted) {
        QUser qUser =QUser.user;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.deleted.eq(deleted));
        if(phone != null && !phone.isEmpty()){
            builder.and(qUser.phone.like(phone));
        }
        if(!StringUtils.isEmpty(name)){
            builder.and(qUser.name.like("%"+name+"%"));
        }
        return query().select(qUser).from(qUser)

                .where(builder)
                .orderBy(qUser.id.desc())
                .offset(page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .fetch();
    }

    @Override
    public List<User> getListUser2() {
        QUser qUser =QUser.user;
        BooleanBuilder builder = new BooleanBuilder();

        return query().select(qUser).from(qUser).where(builder)
                .orderBy(qUser.id.desc())
                .fetch();
    }

    @Override
    public Long countListUser(String phone, String name, boolean deleted) {
        QUser qUser =QUser.user;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.deleted.eq(deleted));
        if(phone != null && !phone.isEmpty()){
            builder.and(qUser.phone.like(phone));
        }
        if(!StringUtils.isEmpty(name)){
            builder.and(qUser.name.like("%"+name+"%"));
        }
        return query().select(qUser).from(qUser).where(builder)
                .fetchCount();
    }
}
