package org.moshe.arad.services;

import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LobbyViewUpdateJob implements Callable<Boolean>{

	private String keyToFind;
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
    public LobbyViewUpdateJob(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
	@Override
	public Boolean call() throws Exception {
		Boolean result = null;
		
		while(true){
			result = redisTemplate.hasKey(keyToFind);
			if(result == true) return result;
			else Thread.sleep(10);
		}
	}

	public String getKeyToFind() {
		return keyToFind;
	}

	public void setKeyToFind(String keyToFind) {
		this.keyToFind = keyToFind;
	}
}
