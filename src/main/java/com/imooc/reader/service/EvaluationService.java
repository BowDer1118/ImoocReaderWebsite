package com.imooc.reader.service;

import com.imooc.reader.entity.Evaluation;

import java.util.List;

public interface EvaluationService {
    /**
     * 按照書本id查詢有效的評論
     * @param bookId 書本id
     * @return 評論List
     */
    public List<Evaluation> selectByBookId(Long bookId);
}
