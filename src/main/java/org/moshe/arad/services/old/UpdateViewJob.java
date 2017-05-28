package org.moshe.arad.services.old;

import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UpdateViewJob implements Callable<Set<String>>{

	private RedisTemplate<String, String> redisTemplate;
	public static final String NEED_TO_UPDATE = "NeedToUpdate";
	
	@Autowired
    public UpdateViewJob(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
	@Override
	public Set<String> call() throws Exception {
		Set<String> keys = null;
		
		while(true){
			keys = redisTemplate.keys(NEED_TO_UPDATE + ":*");
			if(keys != null && !keys.isEmpty()){
				break;
			}
			else Thread.sleep(50);
		}
		return keys;
	}
}
