package com.kkk.sbgtest;

import com.kkk.sbgtest.model.TestLbData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestCache {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 캐시 생성시에는 해당 함수를 수행하고, 생성후에는 생성된 값을 바로 리턴 (언제까지? 아래 evict를 호출할 때까지)
    @Cacheable(cacheNames = "cache1")
    public TestLbData getCache1() {
        logger.info("## TestCache.java [getCache1] starts..");
        TestLbData lData = new TestLbData("a","b","c");

        return lData;
    }

    @CacheEvict(cacheNames = "cache1", allEntries = true)
    public void evictCache1() {
        logger.info("## TestCache.java [evictCache1] starts..");
    }

}
