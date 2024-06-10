package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.UploadFile;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.FileStorageService;
import com.jason.elearning.util.KeystoreUtils;
import com.jason.elearning.util.MultipartFileSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;

@RestController
@RequestMapping("/api/")
@Transactional
public class FileController extends BaseController{
    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping("/sign-pdf")
    public ResponseEntity<?> signPdf(@RequestParam("pdf") MultipartFile pdfFile) throws Exception {
        byte[] pdfData = pdfFile.getBytes();

        KeyStore keystore = KeystoreUtils.loadKeyStore("src/main/resources/keystore.jks", "jasonanh1");
        PrivateKey privateKey = KeystoreUtils.getPrivateKey(keystore, "mykeyalias", "jasonanh1");
        byte[] signedPdfData = KeystoreUtils.signData(privateKey, pdfData);

        Resource signedPdfResource = new ByteArrayResource(signedPdfData);

// Sử dụng signedPdfData thay vì pdfData
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(new InputStreamResource(new ByteArrayInputStream(signedPdfData)));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(signedPdfData));
    }

        @PostMapping("/verify-signature")
    public ResponseEntity<?> verifySignature(@RequestParam("pdf") MultipartFile pdfFile) throws Exception {
        byte[] signedPdfData = pdfFile.getBytes();

        int signatureLength = 256; // Độ dài của chữ ký (phụ thuộc vào thuật toán và độ dài khóa)
        int pdfDataLength = signedPdfData.length - signatureLength;
        byte[] pdfData = new byte[pdfDataLength];
        byte[] signature = new byte[signatureLength];
        System.arraycopy(signedPdfData, 0, pdfData, 0, pdfDataLength);
        System.arraycopy(signedPdfData, pdfDataLength, signature, 0, signatureLength);


        KeyStore keystore = KeystoreUtils.loadKeyStore("src/main/resources/keystore.jks", "jasonanh1");
        Certificate certificate = keystore.getCertificate("mykeyalias");
        PublicKey publicKey = certificate.getPublicKey();

        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(pdfData);

        return ResponseEntity.ok(verifier.verify(signature));
    }
    @PostMapping("v1/file/upload-image")
    public ResponseEntity<?> uploadImage1(HttpServletRequest httpServletRequest, @RequestParam("file") final MultipartFile file) {
        try {
            if(file == null || httpServletRequest == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            if(file.getSize() > 1024*1024*20) {
                throw new Exception("Dung lượng file quá lớn, vui lòng chọn file nhỏ hơn 20MB");
            }
            UploadFile uploadFile = fileStorageService.storeImage(httpServletRequest, file);
            return ResponseEntity.ok(new BaseResponse(Translator.toLocale("succecss"), uploadFile));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("download/{fileName:.+}")
    public ResponseEntity<?> getVideo(@PathVariable final String fileName) throws Exception {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(resource.getInputStream()));
    }

    @PostMapping("v1/file/upload-file")
    public ResponseEntity<?> uploadFile(HttpServletRequest httpServletRequest, @RequestParam("file") final MultipartFile file) {
        try {
            if(file == null || httpServletRequest == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            if(file.getSize() > 1024*1024*20) {
                throw new Exception("Dung lượng file quá lớn, vui lòng chọn file nhỏ hơn 20MB");
            }
            UploadFile uploadFile = fileStorageService.storeFile(httpServletRequest, file);
            return ResponseEntity.ok(new BaseResponse(Translator.toLocale("succecss"), uploadFile));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("images/{fileName:.+}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable final String fileName) throws Exception {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(resource.getInputStream()));
    }

    @PostMapping("v1/file/upload-video")
    public ResponseEntity<?> uploadVideo1(HttpServletRequest httpServletRequest,
                                          @RequestParam("thumbFile") final MultipartFile thumbFile,
                                          @RequestParam("videoFile") final MultipartFile videoFile) {
        try {
            if(thumbFile == null || videoFile == null || httpServletRequest == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
//            if(videoFile.getSize() > 1024*1024*20 || thumbFile.getSize() > 1024*1024*20) {
//                throw new Exception("Dung lượng file quá lớn, vui lòng chọn file nhỏ hơn 20MB");
//            }
            UploadFile uploadFile = fileStorageService.storeVideo(httpServletRequest, thumbFile, videoFile);
            return ResponseEntity.ok(new BaseResponse(Translator.toLocale("succecss"), uploadFile));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("video/{fileName:.+}")
    public void streamVideo(HttpServletResponse response, HttpServletRequest request, @PathVariable final String fileName) throws Exception {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String path = resource.getFile().getPath();
        try {
            MultipartFileSender.fromFile(new File(path))
                    .with(request)
                    .with(response)
                    .serveResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

