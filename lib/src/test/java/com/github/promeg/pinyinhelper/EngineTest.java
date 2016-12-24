package com.github.promeg.pinyinhelper;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by guyacong on 2016/12/23.
 */
public class EngineTest {

    List<PinyinDict> mPinyinDicts;

    @Before
    public void setUp() {
        mPinyinDicts = new ArrayList<PinyinDict>();
    }

    @Test
    public void toPinyin_withOneDict() throws Exception {
        mPinyinDicts.add(new PinyinDict() {
            @Override
            public Map<String, String[]> mapping() {
                Map<String, String[]> map = new HashMap<String, String[]>();
                map.put("重庆", new String[]{"CHONG", "QING"});
                map.put("长安", new String[]{"CHANG", "AN"});
                return map;
            }
        });
        String result = Engine.toPinyin("重庆和长安都很棒!", mPinyinDicts, ",");

        String expect = "CHONG,QING,HE,CHANG,AN,DOU,HEN,BANG,!";
        assertThat(expect, is(result));
    }

    @Test
    public void toPinyin_withZeroDict() throws Exception {
        mPinyinDicts.clear();
        String result = Engine.toPinyin("重庆和长安都很棒!", mPinyinDicts, ",");

        String expect = "ZHONG,QING,HE,ZHANG,AN,DOU,HEN,BANG,!";
        assertThat(expect, is(result));
    }

    @Test
    public void toPinyin_withNullDict() throws Exception {
        String result = Engine.toPinyin("重庆和长安都很棒!", null, ",");

        String expect = "ZHONG,QING,HE,ZHANG,AN,DOU,HEN,BANG,!";
        assertThat(expect, is(result));
    }

    @Test
    public void toPinyin_withMultiDict() throws Exception {
        // First one wins，按照顺序匹配
        mPinyinDicts.add(new PinyinDict() {
            @Override
            public Map<String, String[]> mapping() {
                Map<String, String[]> map = new HashMap<String, String[]>();
                map.put("重庆", new String[]{"CHONG", "QING"});
                return map;
            }
        });

        mPinyinDicts.add(new PinyinDict() {
            @Override
            public Map<String, String[]> mapping() {
                Map<String, String[]> map = new HashMap<String, String[]>();
                map.put("重庆", new String[]{"NOT", "MATCH"});
                map.put("长安", new String[]{"CHANG", "AN"});
                return map;
            }
        });
        String result = Engine.toPinyin("重庆和长安都很棒!", mPinyinDicts, ",");

        String expect = "CHONG,QING,HE,CHANG,AN,DOU,HEN,BANG,!";
        assertThat(expect, is(result));
    }

}