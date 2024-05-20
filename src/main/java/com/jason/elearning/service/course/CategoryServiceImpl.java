package com.jason.elearning.service.course;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Category;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.CategoryType;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.repository.course.CourseCategoryRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends BaseService implements CategoryService {
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;
    @Override
    public Category createCourseCategory(Category category) throws Exception {

        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() == RoleName.ROLE_LECTURE || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        return courseCategoryRepository.save(category);
    }

    @Override
    public List<Category> listCourseCategory() throws Exception {

        return courseCategoryRepository.findAll()
                .stream()
                .filter(category -> category.getType().equals(CategoryType.COURSE))
                .collect(Collectors.toList());
    }

    @Override
    public Category updateCourseCategory(Category request) throws Exception {

        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() == RoleName.ROLE_LECTURE || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Category category = courseCategoryRepository.findById(request.getId()).orElseThrow(()-> new Exception("can not find category"));
        category.setDescription(request.getDescription());
        category.setTitle(request.getTitle());
        category.setDeleted(request.getDeleted());
        return category;
    }
}
