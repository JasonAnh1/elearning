package com.jason.elearning.service.user;


import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.*;
import com.jason.elearning.entity.constants.EnrollStatus;
import com.jason.elearning.entity.constants.PlanType;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.entity.constants.UserActive;
import com.jason.elearning.entity.request.EnrollRequest;
import com.jason.elearning.entity.request.PlanCourseRequest;
import com.jason.elearning.repository.course.CoursePartRepository;
import com.jason.elearning.repository.course.LessonRepository;
import com.jason.elearning.repository.enroll.LessonProgressRepository;
import com.jason.elearning.repository.plan.PlanCourseRepository;
import com.jason.elearning.repository.plan.PlanRepository;
import com.jason.elearning.repository.transaction.TransactionRepository;
import com.jason.elearning.repository.user.EnrollRepository;
import com.jason.elearning.repository.user.RoleRepository;
import com.jason.elearning.repository.user.UserRepository;
import com.jason.elearning.security.CustomUserDetailsService;
import com.jason.elearning.security.JwtTokenProvider;
import com.jason.elearning.security.UserPrincipal;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
class UserServiceImpl extends BaseService implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private EnrollRepository enrollRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanCourseRepository planCourseRepository;

    @Autowired
    private CoursePartRepository coursePartRepository;

    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /////////////////////User///////////


    @Override
    public void test() throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER ) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
    }

    public User signin(User request) throws Exception {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
        } catch (Exception e) {
            throw new Exception(Translator.toLocale("login_fail"));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByPhone(request.getPhone()).orElseThrow(() -> new UsernameNotFoundException(String.format(Translator.toLocale("user_not_found_with_phone"), request.getPhone())));
        if (user.getDeleted()) {
            throw new Exception("Tài khoản đã bị xoá");
        }
        String jwt = tokenProvider.generateToken(authentication);

        if(user.getOrganizationId() != null)
        {
            User org = userRepository.findUserById(user.getOrganizationId());
            user.setOrganization(org);
        }
        user.setAccessToken(jwt);
        return user;
    }

    @Override
    public User signup(User request) throws Exception {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new Exception(Translator.toLocale("phone_exists"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception(Translator.toLocale("email_exists"));
        }
        if (userRepository.existsByName(request.getName())) {
            throw new Exception(Translator.toLocale("name_exists"));
        }


        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_LEARNER).orElseThrow(() -> new RuntimeException("User Role not set."));
        request.setRoles(Collections.singleton(userRole));
        User result = userRepository.save(request);
//        sendNotification(result);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(result.getPhone());
        String jwt = tokenProvider.generateTokenByUser((UserPrincipal) userDetails);
        result.setAccessToken(jwt);
        return result;
    }

    @Override
    public User signupaslecture(User request) throws Exception {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new Exception(Translator.toLocale("phone_exists"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception(Translator.toLocale("email_exists"));
        }
        if (userRepository.existsByName(request.getName())) {
            throw new Exception(Translator.toLocale("name_exists"));
        }

        request.setActive(UserActive.UNVERIFY);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_LECTURE).orElseThrow(() -> new RuntimeException("User Role not set."));
        request.setRoles(Collections.singleton(userRole));
        User result = userRepository.save(request);
//        sendNotification(result);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(result.getPhone());
        String jwt = tokenProvider.generateTokenByUser((UserPrincipal) userDetails);
        result.setAccessToken(jwt);
        return result;
    }

    @Override
    public User signUpAsLearner(User request) throws Exception {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new Exception(Translator.toLocale("phone_exists"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception(Translator.toLocale("email_exists"));
        }
        if (userRepository.existsByName(request.getName())) {
            throw new Exception(Translator.toLocale("name_exists"));
        }

        request.setActive(UserActive.UNVERIFY);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_LEARNER).orElseThrow(() -> new RuntimeException("User Role not set."));
        request.setRoles(Collections.singleton(userRole));
        User result = userRepository.save(request);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(result.getPhone());
        String jwt = tokenProvider.generateTokenByUser((UserPrincipal) userDetails);
        result.setAccessToken(jwt);
        return result;
    }

    @Override
    public User signUpAsOrganization(User request) throws Exception {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new Exception(Translator.toLocale("phone_exists"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception(Translator.toLocale("email_exists"));
        }
        if (userRepository.existsByName(request.getName())) {
            throw new Exception(Translator.toLocale("name_exists"));
        }

        request.setActive(UserActive.UNVERIFY);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_ORGANIZATION).orElseThrow(() -> new RuntimeException("User Role not set."));
        request.setRoles(Collections.singleton(userRole));
        User result = userRepository.save(request);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(result.getPhone());
        String jwt = tokenProvider.generateTokenByUser((UserPrincipal) userDetails);
        result.setAccessToken(jwt);
        return result;
    }

    @Override
    public User getUserInfo(Long userId) throws Exception {
        User me = getUser();
        if (userId == null || me.getId() == userId) {
            return me;
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("Cannot find user"));
        User response = new User();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAvatar(user.getAvatar());
        return response;
    }

    @Override
    public User updateProfile(User request) throws Exception {
        User user = userRepository.findById(request.getId()).orElseThrow(() -> new Exception("Cannot find user"));
        if(request.getEmail() != null){
            user.setEmail(request.getEmail());
        }
        if(request.getPhone() != null){
            user.setPhone(request.getPhone());
        }
        if(request.getAvatarId() !=  null){
            user.setAvatarId(request.getAvatarId());
        }
        return userRepository.save(user);
    }

    @Override
    public List<Enroll> enrollACourse(EnrollRequest request) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        List<Enroll> enrollList = new LinkedList<>();
        for(long courseId:request.getCourseIds()){
            Enroll e = new Enroll();
            e.setCourseId(courseId);
            e.setUserId(user.getId());
            e.setStatus(EnrollStatus.ENROLLED);
            enrollList.add(e);
        }
        return  enrollRepository.saveAll(enrollList);
    }

    @Override
    public User verifyLecture(long id) throws Exception {
            User user = userRepository.findById(id).orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
        user.setActive(UserActive.VERIFY);
        return user;
    }

    @Override
    public User adminAddUser(User request) throws Exception {
        return null;
    }

    @Override
    public User adminSignin(User request) throws Exception {
        return null;
    }

    @Override
    public List<User> getListUser2() throws Exception {

        return userRepository.getListUser2();
    }

    @Override
    public List<User> getListUser(int page, String phone, String name, boolean deleted) throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER ) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        return userRepository.getListUser(page, phone, name,deleted);
    }

    @Override
    public Long countListUser(int page, String phone, String name, boolean deleted) throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        return userRepository.countListUser(phone, name,deleted);
    }

    @Override
    public User addToOrganization(User request) throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() != RoleName.ROLE_ORGANIZATION) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Plan plan = planRepository.findFirstByOrganizationIdOrderByCreatedAtDesc(user.getId());
        if(plan == null){
            throw new Exception(Translator.toLocale("you must have a plan before adding member"));
        }
        long totalMembers = userRepository.findALlByOrganizationId(user.getId()).size();
        if(plan.getType() == PlanType.STARTER && totalMembers >= 10){
            throw new Exception(Translator.toLocale("starter plan limited member"));
        } else if(plan.getType() == PlanType.POPULAR && totalMembers >= 20)
        {
            throw new Exception(Translator.toLocale("popular plan limited member"));
        }

        User addOrganizationUser = userRepository.findUserById(request.getId());
        addOrganizationUser.setOrganizationId(user.getId());
        List<Long> listCourseId = planCourseRepository.findAllByPlanId(plan.getId())
                .stream()
                .map(PlanCourse::getCourseID)
                .collect(Collectors.toList());
        lessonProgressRepository.saveAll( createProgressForNewMember(addOrganizationUser.getId(),listCourseId));

        return addOrganizationUser;
    }

    @Override
    public List<User> listOrganizationMember(Long id) throws Exception {
        return userRepository.findALlByOrganizationId(id);
    }

    @Override
    public List<User> getListUserForOrg(String name) throws Exception {
        return userRepository.getListUserForOrg(name);
    }

    @Override
    public List<User> listOrganizations(UserActive active,String name) throws Exception {
        return userRepository.getListOrganizations(active,name);
    }

    @Override
    public List<PlanCourse> addPlanCourse(PlanCourseRequest request) throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() != RoleName.ROLE_ORGANIZATION) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Plan plan = planRepository.findFirstByOrganizationIdOrderByCreatedAtDesc(user.getId());
        long totalCourseInPlan = planCourseRepository.findAllByPlanId(plan.getId()).size();

        // kiem tra so luong course trong plan co vuot qua khong
        if(plan.getType() == PlanType.STARTER){
            if(totalCourseInPlan + request.getIds().size() > 5 ){
                throw new Exception(Translator.toLocale("reach plan limited course"));
            }
        } else if(plan.getType() == PlanType.POPULAR){
            if(totalCourseInPlan + request.getIds().size() > 10 ){
                throw new Exception(Translator.toLocale("reach plan limited course"));
            }
        }

        // tao 1 list cac lesson progress cho cac user trong ORG
        List<LessonProgress> lessonProgresses = new ArrayList<>();
        // lay tat ca member trong org
        List<Long> members = userRepository.findALlByOrganizationId(user.getId())
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<PlanCourse> planCourses = new ArrayList<>();
        request.getIds().forEach(
                courseId -> {
                    PlanCourse pc = new PlanCourse();
                    pc.setCourseID(courseId);
                    pc.setPlanId(plan.getId());
                    planCourses.add(pc);

                    lessonProgresses.addAll( createProgressForOrgMember(members,courseId));

                }
        );
        lessonProgressRepository.saveAll(lessonProgresses);
        return planCourseRepository.saveAll(planCourses);
    }
    public List<LessonProgress> createProgressForNewMember(Long userId,List<Long> courseIds){
        List<LessonProgress> progressList  = new ArrayList<>();

        courseIds.forEach(courseId ->{
            List<Long> lessonIds = listLessonId(courseId);
            if(!checkUserLessonProgress(lessonIds, userId)){
                List<LessonProgress> list = lessonIds
                        .stream()
                        .map(lessonId -> new LessonProgress().builder()
                                .lessonId(lessonId)
                                .locked(false)
                                .userId(userId)
                                .build()).collect(Collectors.toList());

                progressList.addAll(list);
            }
        });

        return progressList;
    };
    public List<LessonProgress> createProgressForOrgMember(List<Long> userIds,Long courseId){

        List<Long> lessonIds = listLessonId(courseId);
        List<LessonProgress> progressList  = new ArrayList<>();
        userIds.forEach(
                id ->{
                    // chi tao progress neu member nay chua tu mua khoa hoc
                    if(!checkUserLessonProgress(lessonIds, id)){
                        List<LessonProgress> list = lessonIds
                                .stream()
                                .map(lessonId -> new LessonProgress().builder()
                                        .lessonId(lessonId)
                                        .locked(false)
                                        .userId(id)
                                        .build()).collect(Collectors.toList());

                        progressList.addAll(list);
                    }
                }
        );

        return progressList;
    };

    boolean checkUserLessonProgress(List<Long> lessonIds,long userId){
        return lessonIds.stream()
                // Kiểm tra xem có bất kỳ lessonId nào đã tồn tại LessonProgress với userId
                .anyMatch(lessonId -> lessonProgressRepository.existsByLessonIdAndUserId(lessonId, userId));
    }

    public List<Long> listLessonId(Long courseId){
        List<Long> lessonIds  = new ArrayList<>();
        List<CoursePart> lstCoursePart = coursePartRepository.findAllByCourseId(courseId);
        if(lstCoursePart.size() > 0 ){
            lessonIds =  lstCoursePart
                    .stream()
                    .map(coursePart -> coursePart.getLessons())
                    .flatMap(Collection::stream)
                    .map(lesson -> lesson.getId()).collect(Collectors.toList());
        }

        return lessonIds;
    };

    @Override
    public Plan getPlan() throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() != RoleName.ROLE_ORGANIZATION) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        return planRepository.findFirstByOrganizationIdOrderByCreatedAtDesc(user.getId());
    }

    @Override
    public User removeMember(Long userId) throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() != RoleName.ROLE_ORGANIZATION) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        User removeUser = userRepository.findFirstByOrganizationIdAndId(user.getId(),userId);
        removeUser.setOrganizationId(null);

        // tien hanh xoa toan bo cac tien trinh cua khoa hoc ma nguoi dung thuoc org do,
        // cac khoa hoc khong tu mua khi bi remove khoi org
        Plan plan = planRepository.findFirstByOrganizationIdOrderByCreatedAtDesc(user.getId());
        // tim cac khoa hoc cua org
        List<Long> listCourseId = planCourseRepository.findAllByPlanId(plan.getId())
                .stream()
                .map(PlanCourse::getCourseID)
                .collect(Collectors.toList());

        // tim cac khoa hoc cua nguoi dung tu enroll
        List<Long> enrollList = enrollRepository.findByUserId(userId).stream()
                .map(Enroll::getCourseId)
                .collect(Collectors.toList());

        // lay ra cac khoa hoc ma cua org chứ người dùng không đăng ký
        listCourseId.removeAll(enrollList);
        // tien hanh xoa progress
        List<CoursePart> coursePartList = new ArrayList<>();
        listCourseId.forEach(
                courseId -> {
                    List<CoursePart> temp = coursePartRepository.findAllByCourseId(courseId);
                    coursePartList.addAll(temp);
                }
        );

        List<Long> listLessonId = lessonRepository
                .listLessonByListCoursePartId(coursePartList
                        .stream()
                        .map(CoursePart::getId)
                        .collect(Collectors.toList()))
                .stream()
                .map(Lesson::getId)
                .collect(Collectors.toList());

        List<LessonProgress> lessonProgressList = new ArrayList<>();

        listLessonId.forEach(
                lessonId ->{
                    lessonProgressList.add(
                            lessonProgressRepository.findFirstByUserIdAndLessonId(userId,lessonId)
                    );
                }
        );

        lessonProgressRepository.deleteAll(lessonProgressList);
        return removeUser;
    }

    @Override
    public List<User> getListLecturer(String name) throws Exception {

        return userRepository.findUsersByRoleName(RoleName.ROLE_LECTURE);
    }


}
