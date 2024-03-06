package com.jason.elearning.service.course;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.CourseCategory;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.repository.course.CourseCategoryRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCategoryServiceImpl extends BaseService implements CourseCategoryService{
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;
    @Override
    public CourseCategory createCourseCategory(CourseCategory courseCategory) throws Exception {

        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() == RoleName.ROLE_LECTURE || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        return courseCategoryRepository.save(courseCategory);
    }

    @Override
    public List<CourseCategory> listCourseCategory() throws Exception {

        return courseCategoryRepository.findAll();
    }

    @Override
    public CourseCategory updateCourseCategory(CourseCategory request) throws Exception {

        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() == RoleName.ROLE_LECTURE || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        CourseCategory category = courseCategoryRepository.findById(request.getId()).orElseThrow(()-> new Exception("can not find category"));
        category.setDescription(request.getDescription());
        category.setTitle(request.getTitle());
        category.setDeleted(request.getDeleted());
        return category;
    }
}
