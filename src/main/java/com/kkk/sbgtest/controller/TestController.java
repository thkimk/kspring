package com.kkk.sbgtest.controller;

import com.kkk.sbgtest.NotifyData;
import com.kkk.sbgtest.TestCache;
import com.kkk.sbgtest.TestException;
import com.kkk.sbgtest.dao.Test2Dao;
import com.kkk.sbgtest.kafka.Ping;
import com.kkk.sbgtest.kafka.PingService;
import com.kkk.sbgtest.mongodb.MongoMyService;
import com.kkk.sbgtest.mq.EmailProducer;
import com.kkk.sbgtest.service.AsyncService;
import com.kkk.sbgtest.service.RedisService;
import com.kkk.sbgtest.service.TestUtils;
import com.kkk.sbgtest.dao.TestDao;
import com.kkk.sbgtest.model.SearchVO;
import com.kkk.sbgtest.model.TestLbData;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class TestController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // config 파일에서 변수 가져오기
    @Value("${test.base}")
    private String testBase;

    @Autowired
    private TestDao testDao;

    @Autowired
    private Test2Dao test2Dao;

    @Autowired
    private TestCache testCache;

    @Autowired
    private TestUtils testUtils;

//    @Autowired
//    private UserRepository userRepository;

//    @Autowired
//    private AirRepository airRepository;

    @Autowired
    private RedisService redisService;

//    @Autowired
//    private MysqlService mysqlService;

    @Autowired
    private EmailProducer emailProducer;

    @Autowired
    private PingService pingService;

    @Autowired
    private MongoMyService mongoMyService;

    private static NotifyData notifyData = new NotifyData();

    @GetMapping("/test1")    // http://localhost:8090/test
    public Map<String, Object> getTest1() {
        logger.info("test1");
        logger.info("*************@RequestHeader HttpHeaders headers***************");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("a",1);
        resultMap.put("b",2);
        resultMap.put("c",3);

        ArrayList<HashMap> lData = testDao.test1();
        for (HashMap lHash : lData) {
            logger.info("test2");
        }
//        testDao.deleteData();

        return resultMap;
    }

    @GetMapping("/test12")    // http://localhost:8090/test
    public Map<String, Object> getTest12() {
        logger.info("test1");
        logger.info("*************@RequestHeader HttpHeaders headers***************");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("a",1);
        resultMap.put("b",2);
        resultMap.put("c",3);

        ArrayList<HashMap> lData = test2Dao.test1();
        for (HashMap lHash : lData) {
            logger.info("test2");
        }
        int lRet = test2Dao.deleteData();

        return resultMap;
    }

    @GetMapping("/test2")    // http://localhost:8090/test
    public Object getTest2() throws TestException {
        TestLbData lData = new TestLbData("n","a","adr");
        lData.setAge("22");

        logger.warn("getTest2: [1]");
        if (true) {
            throw new TestException();
        }

        logger.warn("getTest2: [end]");
        return lData;
    }

    @GetMapping("/test3")    // http://localhost:8090/test
    public String getTest3() throws TestException {
        try {
            AsyncService lAsync = new AsyncService();
            System.out.println("## TestControllerTest.java [asyncTest] -- 1");
//            lAsync.async1();
            System.out.println("## TestControllerTest.java [asyncTest] -- 2");
        }
        catch (Exception e) {}
        return testBase;
    }

    @GetMapping(value="/test4/{type}")
    public String test4(@PathVariable("type") String type) {
        logger.warn("test4: [type] "+ type);

        return type;
    }

    @GetMapping("/getParams1")  // http://localhost:8090/getParameters?id=id1&email=email2
    public String getParams1(@RequestParam String id, @RequestParam String email) {
        return "아이디는 "+id+" 이메일은 "+email;
    }


    @GetMapping("/getParams2")  // http://localhost:8090/getMultiParameters?name=id1&address=email2
    public String getParams2(@RequestBody SearchVO searchVo) {
        return "VO사용 아이디는 "+searchVo.getId()+" 이메일은 "+searchVo.getEmail();
    }


    @GetMapping("/getParams3")  // http://localhost:8090/getMultiParameters?name=id1&address=email2
    public String getParams3(@RequestParam HashMap<String,String> p_link) {
        return "VO사용 아이디는 "+p_link.get("id")+" 이메일은 "+p_link.get("address");
    }

    @PostMapping(value = "/postMapping")
    public SearchVO postMapping(@RequestBody SearchVO searchVo){
        return searchVo;
    }


    /**
     * wait/notify 처리 원리 : 특정 Data객체에 대한 wait와 nofity를 제어
     * 테스트 방법 : wailt를 먼저 호출하고 나서, nofity를 나중에 호출하면, 앞의 wait가 뒤의 notify에 의해서 풀림
     * @return
     * @throws TestException
     */
    @GetMapping("/wait")
    public Object wait1() throws TestException {
        logger.warn("wait1(): before");
        notifyData.doWait();
        logger.warn("wait1(): after");

        return "Wait";
    }

    @GetMapping("/notify")
    public Object notify1() throws TestException {
        logger.warn("notify1(): before");
        notifyData.doNotify();
        logger.warn("notify1(): after");

        return "Wait";
    }


    @GetMapping("/getCache")
    public Object getCache() {
        return testCache.getCache1();
    }

    @GetMapping("/evictCache")
    public Object evictCache() {
        testCache.evictCache1();

        return "evictCache";
    }

    //@Transactional(propagation =?? , isolation = ??,noRollbackFor = ??,readOnly = ??,rollbackFor = ??,timeout = ??)
    @Transactional(value="transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @GetMapping("/transationalTest")
    public Object transationalTest() {
        return "transationalTest";
    }

    @GetMapping("/restfulApi")
    public Object restfulApi() {
        return testUtils.utRestApi();
    }


    @GetMapping("/jpaTest")
    public Object jpaTest() {
/*
        Optional<Usertb> user = userRepository.findById(1L);

        user.ifPresent(selectUser ->{
            logger.info("user: "+selectUser);
        });
*/
//        List<AirVO> lList = airRepository.findByName("aaa");

        return "jpaTest";
    }


    @GetMapping("/redisGet")
    public Object redisGet() {
        List<String> users = redisService.getUsernameList();

        return "redisGet";
    }


    @GetMapping("/testException")
    public Object testException() {
        try {
            if (true) throw new TestException();
        }
        catch(TestException e) {
            logger.info("testException: catch: "+ e.toString());
            return "catch"; // 나가는데, 아래 finally를 호출한 후 나가..
                            // 없으면, 아래 return까지 흘러감..
        }
        finally {
            logger.info("testException: finally: ");
        }

        return "testException";
    }

    @GetMapping("/testException2")
    public Object testException2() throws TestException {
        if (true) throw new TestException();

        return "testException2";
    }

    @GetMapping("/mariadb")
    public Object mariadb() {
        return "mariadb";
    }


    @GetMapping("/mysqldb")
    public Object mysqldb() {
        ArrayList<HashMap> lData = testDao.test1();
        for (HashMap lHash : lData) {
            logger.info("test2+ "+ lHash.get("b2b_nm"));
        }
//        mysqlService.selectB2bBas();
        return "mysqldb";
    }

    @GetMapping("/mqtest")
    public void mqtest() {
        emailProducer.sendMsg("info@example.com", "Hello");
    }

    @GetMapping("/kafkatest")
    public void kafkatest() {
        try {
            Ping lPing = new Ping("thkim", "kkk");
            pingService.pingAndPong(lPing);
        } catch (Exception e) {}
    }

    @GetMapping("/mongodb")
    public void mongodb() {
        mongoMyService.testMongo();
    }


    @GetMapping("/rest/test1")
    public String restTest1() {
        return "restTest1";
    }

    @GetMapping("/rest/test2")
    public String restTest2() {
        return "restTest2";
    }

}
