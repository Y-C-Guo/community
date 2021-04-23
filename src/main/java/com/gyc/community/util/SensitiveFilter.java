package com.gyc.community.util;

import com.sun.org.apache.bcel.internal.generic.FSUB;
import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {


    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符
    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){

        try(
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));)
        {
            String keyword;
            while((keyword = reader.readLine())!=null){
                //添加到前缀树
                this.addKeyword(keyword);
            }
        }
        catch (IOException e) {
            logger.error("加载敏感词失败："+e.getMessage());
        }


    }


    //将一个敏感词添加到前缀树中去
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        for(int i = 0;i<keyword.length();i++){
            Character ch = keyword.charAt(i);
            if(tempNode.getSubNode(ch) == null) {
                TrieNode subNode = new TrieNode();
                tempNode.addSubNode(ch,subNode);
                tempNode = subNode;
            }
        }
        tempNode.isKeywordEnd = true;

    }

    /**
     * 过滤敏感词
     * @param text 带过滤的文本
     * @return 过滤后的文本
     */
    //检索并处理敏感词
    public String filter(String text){
        //判空
        if(text == null) return text;

        TrieNode tmpNode = rootNode;//前缀树的节点
        int st = 0;//敏感词开始索引
        int ed = 0;//敏感词结束索引
        StringBuilder sb = new StringBuilder();
        while(st<text.length() && ed<text.length()){
            char ch = text.charAt(ed);
            //跳过符号
            if(isSymbol(ch)){
                if(st==ed){
                    sb.append(ch);
                    st++;
                    ed++;
                }else{
                    ed++;
                }
                continue;

            }
            TrieNode subNode = tmpNode.getSubNode(ch);
            //如果没有对应子节点，证明不是敏感词，直接写入sb指针往后移
            if(subNode == null){
                //st索引处的值不是敏感词
                sb.append(text.charAt(st));
                tmpNode = rootNode;
                ed = st+1;
                st++;
            }else{
                //如果有对应节点，先判断敏感词标记，如果是敏感词，sb里替换，指针往后移
                if(subNode.isKeywordEnd){
                    sb.append(REPLACEMENT);
                    st = ed+1;
                    ed = st;
                    tmpNode = rootNode;
                }else{
                    tmpNode = subNode;
                    ed++;
                }
            }

        }

        return sb.toString();
    }

    //判断是否为符号
    private boolean isSymbol(Character c){
        //c<0x2E80||c>0x9FFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && ((int)c<0x2E80||(int)c>0x9FFF);
    }

    //定义前缀树的结构
    private class TrieNode{
        //关键词结束标识
        private boolean isKeywordEnd = false;
        //当前节点的子节点
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);

        }

        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
