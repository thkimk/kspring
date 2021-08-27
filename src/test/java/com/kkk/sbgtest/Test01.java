package com.kkk.sbgtest;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test01 {

    @Test
    public void chaneImageName() {
        System.out.println("## fileTest()");
        String lPath = "d:/trans/";
        // 특정 디렉토리의 파일 목록
        File dir = new File(lPath);
        File files[] = dir.listFiles();

        // 파일들 loop
        try {
            int i = 0;
            for (File file : files) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd_HHmmss."); // 날짜 포맷을 지정해주면 됨(SimpleDateFormat 이용)
                System.out.println("file: " + file);
                String lExt = StringUtils.getFilenameExtension(file.toString());
                String lFormat = sf.format(file.lastModified());
                String lNewName = file.getParent()+"/"+ lFormat+ lExt;

                File lNewFile = new File(lNewName);
                if (lNewFile.exists()) {
                    lNewName = file.getParent()+"/"+ lFormat.concat(String.valueOf(i++))+"."+ lExt;
                    lNewFile = new File(lNewName);
                }
                file.renameTo(lNewFile);
            }

        } catch (Exception e) {}

        // 해당 파일의 생성시간을 구해서, 그값으로 파일명 변경하기
    }


    @Test
    public void jasyptTest() {
        final String ENC_KEY = "1b671a64-40d5-491e-99b0-da01ff1f3341";
        // ENC(is4xLAKEWo3Hsy61TJHVzAj11IifDSkw)
        // ENC_KEY = "1b671a64-40d5-491e-99b0-da01ff1f3341";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(ENC_KEY); //암호화에 사용할 키 -> 중요
        config.setAlgorithm("PBEWithMD5AndDES"); //사용할 알고리즘
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        String enc = encryptor.encrypt("1234"); //암호화 할 내용
        String dec = encryptor.decrypt("bSZ6vJRFXPq5Xc0GRcH1deWt0NZKAKs8");
        System.out.println("## "+ enc+ ", "+ dec);
/*
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");    // 고정값
        pbeEnc.setPassword(ENC_KEY);    // 암호화키
        String enc = pbeEnc.encrypt("1234"); //암호화 할 내용
        String dec = pbeEnc.decrypt("is4xLAKEWo3Hsy61TJHVzAj11IifDSkw");
        System.out.println("## "+ enc+ ", "+ dec);
*/
    }


    @Test
    public void fieldTest() {
        FieldClass fieldClass = new FieldClass();
        Class<?> cls = fieldClass.getClass();

        Field publicFields[] = cls.getFields();         // public변수만 리턴
        Field allFields[] = cls.getDeclaredFields();    // public/private 모두 리턴

        try {
            System.out.println("[Public Field]");
            for (Field field : publicFields) {
                String name = field.getName();  // 변수명 --> 변수값은 field.get()
                System.out.println("field name : " + name + ", field value : " + field.get(fieldClass));
            }
            System.out.println();

            // 에러 발생
            for (Field field : allFields) {
                String name = field.getName();
                System.out.println("field name : " + name + ", field value : " + field.get(fieldClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("[All Field]");
                for (Field field : allFields) {
                    // 접근해재
                    field.setAccessible(true);
                    String name = field.getName();
                    System.out.println("field name : " + name + ", field value : " + field.get(fieldClass));
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    class FieldClass {

        private int abc;
        private int bcd;
        private int cde;
        private int def;
        public int efg;
        public int fgh;

        public FieldClass() {
            abc = 10;
            bcd = 11;
            cde = 12;
            def = 13;
            efg = 14;
            fgh = 15;
        }
    }
}
