package com.wings.common.utils.aggregation.sort;

import com.alibaba.fastjson.JSON;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 聚合排序工具类
 * Created by Wings on 2020/7/9.
 */
public class SortUtils {

//    public static void main(String[] args) {
//        // mock data
//        Integer[] rom1 = {1, 2, 3, 0};
//        Integer[] rom2 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
//        List<User> users = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            int index1 = (int) (Math.random() * 4);
//            int index2 = (int) (Math.random() * 9);
//            User user = User.builder()
//                    .userId((long) i)
//                    .code(rom2[index2])
//                    .group(rom1[index1])
//                    .build();
//            users.add(user);
//        }
//
//        List<Integer> beforeGroupList = users.parallelStream().map(e -> e.getGroup()).collect(Collectors.toList());
//        // result = [2,2,2,2,0,1,1,0,2,1,2,1,0,0,1,3,0,0,1,2]
//        System.out.println("before ->   " + JSON.toJSONString(beforeGroupList));
//
//        // 排序枚举条件
//        List<Integer> rules = Arrays.asList(2, 1, 0, 3);
//        users = sortedBySingleClassifySortRule(users, rules, User::getGroup, true);
//
//        List<Integer> afterGroupList = users.parallelStream().map(e -> e.getGroup()).collect(Collectors.toList());
//        // result = [2,2,2,2,2,2,2,1,1,1,1,1,1,0,0,0,0,0,0,3]
//        System.out.println("after ->    " + JSON.toJSONString(afterGroupList));
//
//    }

    /**
     * 单个排序字段 按指定枚举值顺序排序
     * @param list      目标排序集合
     * @param sortRules 排序枚举值规则集合
     * @param sortField 排序字段
     * @param <T>       输入类型
     * @param <R>       输出类型
     * @param isAsc     是否升序
     * @return 排序结果集合 规定
     */
    public static <T, S, R> List<T> sortedBySingleClassifySortRule(List<T> list, List<S> sortRules, Function<? super T, ? extends R> sortField, Boolean isAsc) {
        if (ObjectUtils.isEmpty(list))
            return null;
        if (ObjectUtils.isEmpty(sortRules))
            return list;
        Map<S, Integer> ruleIndexMap = convertSortRulesIndex(sortRules);
        sort(list, Comparator.comparing(e -> getRuleIndex(ruleIndexMap, sortField.apply(e))), isAsc);
        return list;
    }

    /**
     * 获取排序规则角标
     * @param ruleIndexMap 排序规则角标map  {key=排序规则枚举值, value=排序角标}
     * @param applyValue   对应枚举值
     * @param <S>
     * @param <R>
     * @return
     */
    private static <S, R> Integer getRuleIndex(Map<S, Integer> ruleIndexMap, R applyValue) {
        Integer index = ruleIndexMap.get(applyValue);
        if (index == null)
            return Integer.MAX_VALUE;
        return index;
    }

    /**
     * 排序
     * @param <T>
     * @param list       排序集合
     * @param comparator 排序条件
     * @param isAsc      是否升序
     */
    private static <T> void sort(List<? extends T> list, Comparator<? super T> comparator, Boolean isAsc) {
        comparator = isAsc ? comparator : comparator.reversed();
        list.sort(comparator);
    }

    /**
     * 规则类型转换 list to map
     * @param sortRules 排序规则集合
     * @param <S>
     * @return
     */
    private static <S> Map<S, Integer> convertSortRulesIndex(List<S> sortRules) {
        Map<S, Integer> ruleIndexMap = new HashMap<>();
        for (int i = 0; i < sortRules.size(); i++) {
            if (null == sortRules.get(i))
                continue;
            ruleIndexMap.put(sortRules.get(i), i);
        }
        return ruleIndexMap;
    }
}
