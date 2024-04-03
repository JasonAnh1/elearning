package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.LessonComment;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.comment.LessonCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class CommentController {
    @Autowired
    private LessonCommentService lessonCommentService;

    @PostMapping("v1/create-lesson-comment")
        public ResponseEntity<?> createLessonComment(@Valid @RequestBody final LessonComment request) {
        try {
            if(request == null
            || request.getContent() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            lessonCommentService.addLessonComment(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }


    @GetMapping("v1/list-lesson-comment")
    public ResponseEntity<?> listLessonComment(@Valid @RequestParam final Long request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            ;
            return ResponseEntity.ok(lessonCommentService.listLessonComment(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

//    @GetMapping("v1/list-lesson-comment")
//    public ResponseEntity<List<LessonComment>> listLessonComment(@Valid @RequestParam Long request) {
//        try {
//            if(request == null) {
//                throw new Exception(Translator.toLocale("required_fields"));
//            }
//
//            System.out.println(getListRootComment(lessonCommentService.listLessonComment(request)));
//            System.out.println(getChildCommentEachRoot(lessonCommentService.listLessonComment(request)));
//            List<LessonComment> listRootComment =getListRootComment(lessonCommentService.listLessonComment(request));
//            HashMap<LessonComment, List<LessonComment>> childCommentEachRoot =  getChildCommentEachRoot(lessonCommentService.listLessonComment(request));
//            for (LessonComment lessonComment: listRootComment) {
//                List<LessonComment> tempList = null;
//                for( LessonComment ls : childCommentEachRoot.get(lessonComment)){
//                    LessonComment tempComment = null;
//                    tempComment.setContent(ls.getContent());
//                    tempComment.setParentUserName(ls.getParentLessonComment().getUser().getName());
//                    tempList.add(tempComment);
//                }
//
//
//                lessonComment.setLessonCommentList(tempList );
//            }
//
//            return new ResponseEntity<List<LessonComment>>(listRootComment, HttpStatus.OK);
//
//        } catch (Exception ex) {
//            return null;
////            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
//        }
//    }
//
//    public static List<LessonComment> getListRootComment(List<LessonComment> listAllComment) {
//        return listAllComment
//                .stream()
//                .filter(lessonComment -> lessonComment.getParentLessonComment()==null)
//                .collect(Collectors.toList());
//    }
//
//    public  HashMap<LessonComment, List<LessonComment>> getChildCommentEachRoot(List<LessonComment> listAllComment) {
//        HashMap<LessonComment, List<LessonComment>> childCommentEachRoot = new HashMap<>();
//        return recurse(listAllComment, childCommentEachRoot);
//    }
//
//
//
//    public static HashMap<LessonComment, List<LessonComment>> recurse(List<LessonComment> listAllComment,
//                                                                      HashMap<LessonComment, List<LessonComment>> childCommentEachRoot) {
//        for (LessonComment lessonComment: listAllComment) {
//            if (lessonComment.getParentLessonComment() == null) continue;
//            LessonComment temp = lessonComment;
//            while (temp.getParentLessonComment() != null) {
//                temp = temp.getParentLessonComment();
//            }
//            List<LessonComment> child = childCommentEachRoot.getOrDefault(temp, new ArrayList<>());
//            child.add(lessonComment);
//            childCommentEachRoot.put(temp, child);
//        }
//
//        for (List<LessonComment> list: childCommentEachRoot.values()) {
//            list.sort(Comparator.comparing(obj -> obj.getCreatedAt()));
//        }
//
//        for (LessonComment lessonComment: getListRootComment(listAllComment)) {
//            if (childCommentEachRoot.get(lessonComment) == null) {
//                childCommentEachRoot.put(lessonComment, new ArrayList<>());
//            }
//        }
//
//        return childCommentEachRoot;
//    }


    @PostMapping("v1/update-lesson-comment")
    public ResponseEntity<?> updateLessonComment(@Valid @RequestBody final LessonComment request) {
        try {
            if(request == null
                    || request.getContent() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            lessonCommentService.updateLessonComment(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/delete-lesson-comment")
    public ResponseEntity<?> deleteLessonComment(@Valid @RequestParam final Long request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            lessonCommentService.deleteLessonComment(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
