package com.jason.elearning.controller;

import com.jason.elearning.util.KeystoreUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.KeyStore;
import java.security.PrivateKey;

//package com.jason.elearning.controller;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
@RequestMapping("/api/")
public class SignPdfController {
    @PostMapping("/sign-pdf")
    public ResponseEntity<byte[]> signPdf(@RequestParam("pdf") MultipartFile pdfFile) throws Exception {
        byte[] pdfData = pdfFile.getBytes();

        KeyStore keystore = KeystoreUtils.loadKeyStore("src/main/resources/keystore.jks", "jasonanh1");
        PrivateKey privateKey = KeystoreUtils.getPrivateKey(keystore, "mykeyalias", "jasonanh1");
        byte[] signedPdfData = KeystoreUtils.signData(privateKey, pdfData);

        return ResponseEntity.ok(signedPdfData);


        // ... (phần còn lại giống như trước)
    }
//    private static final String KEYSTORE_PATH = "src/main/resources/keystore.jks";
//    private static final String KEYSTORE_PASSWORD = "jasonanh1";
//    private static final String KEY_ALIAS = "mykeyalias";
//
//    @PostMapping("/v1/file/uploadPdf")
//    public ResponseEntity<ByteArrayInputStream> uploadFile(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        try (InputStream is = file.getInputStream(); ByteArrayOutputStream os = new ByteArrayOutputStream()) {
//            // Ký file PDF
//            byte[] signedPdfBytes = signPdf(is, KEYSTORE_PATH, KEYSTORE_PASSWORD, KEY_ALIAS);
//
//            // Trả về file PDF đã ký
//            ByteArrayInputStream signedPdfStream = new ByteArrayInputStream(signedPdfBytes);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=signed_document.pdf");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentType(MediaType.APPLICATION_PDF)
//                    .body(signedPdfStream);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
//    }
//
//    private byte[] signPdf(InputStream src, String keystorePath, String keystorePassword, String keyAlias) throws Exception {
//        try (ByteArrayOutputStream dest = new ByteArrayOutputStream()) {
//            // Load the keystore
//            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//            ks.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());
//
//            // Get the private key and certificate chain
//            PrivateKey pk = (PrivateKey) ks.getKey(keyAlias, keystorePassword.toCharArray());
//            Certificate[] chain = ks.getCertificateChain(keyAlias);
//
//            // Create reader and writer
//            PdfReader reader = new PdfReader(src);
//            PdfWriter writer = new PdfWriter(dest);
//
//            // Create the signature appearance
//            StampingProperties properties = new StampingProperties();
//            properties.useAppendMode();
//            PdfSigner signer = new PdfSigner(reader, writer, properties);
//            PdfSignatureAppearance appearance = signer.getSignatureAppearance()
//                    .setReason("Certificate")
//                    .setLocation("Location")
//                    .setReuseAppearance(false);
//
//            signer.setFieldName("sig");
//
//            // Create the signature
//            IExternalSignature pks = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, "BC");
//            IExternalDigest digest = new BouncyCastleDigest();
//
//            signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);
//
//            return dest.toByteArray();
//        }
//    }
}
