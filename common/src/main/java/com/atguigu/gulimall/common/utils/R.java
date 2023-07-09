/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-07-21 16:08:04
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-13 14:19:18
 * @FilePath: /common/src/main/java/com/atguigu/gulimall/common/utils/R.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.atguigu.gulimall.common.utils;

import org.apache.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.HashMap;
import java.util.Map;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R<T> extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	private T data;

	/**
	 * @param targetClass 用于反序列化数据时指定的类
	 * @return 反序列化后的数据
	 */
	public T getData(TypeReference<T> valueTypeRef) {
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
		Object data = get("data");
		if (data == null) {
			return null;
		}
		try {
			return objectMapper.readValue(objectMapper.writeValueAsString(data), valueTypeRef);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to deserialize data", e);
		}
	}

	/**
	 * @param data 设置请求体
	 * @return
	 */
	public R<T> setData(T data) {
		put("data", data);
		return this;
	}
	public R() {
		put("code", 0);
		put("msg", "success");
	}

	public static R error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	public static R error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok() {
		return new R();
	}

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public int getCode() {
		return (Integer) this.get("code");
	}

	@Override
	public String toString() {
		return "R [data=" + data + "]";
	}

}
