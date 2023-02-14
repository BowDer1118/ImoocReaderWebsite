package com.imooc.reader.service;

import com.imooc.reader.entity.Evaluation;
import com.imooc.reader.entity.Member;
import com.imooc.reader.entity.MemberReadState;

public interface MemberService {
    /**
     * 會員註冊:建立一個會員，並寫入資料庫
     * @param username 用戶名稱
     * @param password 用戶密碼
     * @param nickname 用戶暱稱
     * @return 會員物件
     */
    public Member createMember(String username,String password,String nickname);

    /**
     * 用戶登入:檢查用戶名稱與密碼
     * @param username 用戶名稱
     * @param password 用戶密碼
     * @return 會員物件
     */
    public Member checkLogin(String username,String password);

    /**
     * 依照會員id與書本id查詢會員是否對當前書本評論過
     * @param memberId 會員id
     * @param bookId 書本id
     * @return 會員閱讀狀態物件
     */
    public MemberReadState selectMemberReadState(Long memberId,Long bookId);

    /**
     * 更新閱讀狀態
     * @param memberId 會員id
     * @param bookId 書本id
     * @param readState 閱讀狀態
     * @return 閱讀狀態物件
     */
    public MemberReadState updateMemberReadState(Long memberId,Long bookId,Integer readState);

    /**
     * 發布新的短評
     * @param memberId 會員id
     * @param bookId 書本id
     * @param score 評論的分數
     * @param content 評論的內容
     * @return 評論物件
     */
    public Evaluation evaluate(Long memberId,Long bookId,Integer score,String content);

    /**
     * 短評點讚
     * @param evaluationId 短評編號
     * @return 評論物件
     */
    public Evaluation enjoy(Long evaluationId);
}
