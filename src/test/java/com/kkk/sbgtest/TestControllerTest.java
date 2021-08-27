package com.kkk.sbgtest;

import com.kkk.sbgtest.dao.MysqlIfDao;
import com.kkk.sbgtest.model.B2bBasData;
import com.kkk.sbgtest.model.EnumData;
import com.kkk.sbgtest.mq.EmailProducer;
import com.kkk.sbgtest.service.AsyncService;
import com.kkk.sbgtest.common.CrcUtil;
import com.kkk.sbgtest.service.MysqlService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;


@EnableAsync
class TestControllerTest {
    private AsyncService service = new AsyncService();

    @Autowired
    private MysqlIfDao mysqlIfDao;

    @BeforeEach
    void setUp() {
        System.out.println("Before");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getTest2() {
        String a1 = "hello world";
        System.out.println(a1);
    }

    //@Test
    public void redisTest() {
//        RedisService lService = new RedisService();
//        lService.test();
    }

    @Test
    void getParams1() {
        // 10 00 00 04 77 04 00 00 7B 2A
        // 0x01 0x03 0x00 0x00 0x00 0x06 0xC5 0xC8

        byte[] data = new byte[8];
        data[0] = (byte)0x10;
        data[1] = (byte)0x00;
        data[2] = (byte)0x00;
        data[3] = (byte)0x04;
        data[4] = (byte)0x77;
        data[5] = (byte)0x04;
        data[6] = (byte)0x00;
        data[7] = (byte)0x00;
//        byte[] data = new byte[4];
//        data[0] = (byte)0x12;
//        data[1] = (byte)0x34;
//        data[2] = (byte)0x56;
//        data[3] = (byte)0x78;
//        byte[] data = new byte[6];
//        data[0] = (byte)0x01;
//        data[1] = (byte)0x03;
//        data[2] = (byte)0x00;
//        data[3] = (byte)0x00;
//        data[4] = (byte)0x00;
//        data[5] = (byte)0x06;

        byte[] lCrc = CrcUtil.makeCrc16(data);
        System.out.println("getParams1 is testing now...");
    }


    // 아래 소스는 이상하게 Async로 동작안하네???
    // AsyncController에서 호출하면, 정상적으로 Async로 동작함...
    @Test
    void asyncTest() {
        try {
            AsyncService lAsync = new AsyncService();
            System.out.println("## TestControllerTest.java [asyncTest] -- 1");
            service.onAsync();
            System.out.println("## TestControllerTest.java [asyncTest] -- 2");
        }
        catch (Exception e) {}
    }

    @Test
    void enumTest() {
        String lVal1 = EnumData.REQUEST_AMENITY.getSvcType();
        String lVal2 = EnumData.REQUEST_AMENITY.getPropertyCode();
        String lVal3 = EnumData.REQUEST_AMENITY.getProperty("kkk");
        System.out.println("enum : "+ lVal1+ ", "+ lVal2+ ", "+ lVal3);

    }


    @Test
    void mysqlTest() {
//        MysqlService lMysql = new MysqlService();
//        lMysql.selectB2bBas();
//        B2bBasData lParam = new B2bBasData();
//        List<B2bBasData> lData = mysqlIfDao.selectB2bBas(lParam);
//        System.out.println("## MysqlService.java [selectB2bBas] size: "+ lData.size());
//        lMysql.selectB2bBas();
//        MysqlService lMysql = new MysqlService();
//        lMysql.selectlist();
//        System.out.println("enum : "+ lVal1+ ", "+ lVal2+ ", "+ lVal3);

    }
    
    void etcTest() {
        StringBuffer lBuff = new StringBuffer();
        lBuff.append("");
    }

}