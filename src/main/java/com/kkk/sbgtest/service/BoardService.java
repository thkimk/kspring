package com.kkk.sbgtest.service;

import com.kkk.sbgtest.jpa.BoardRepository;
import com.kkk.sbgtest.jpa.Boardtb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoardService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private BoardRepository boardRepository;

    public Boardtb getBoard(Long boardId) {
/*
        Optional<Boardtb> board = boardRepository.findById(1L);

        board.ifPresent(selectUser ->{
            logger.info("board: "+ selectUser);
        });
*/
        return null;
    }

}