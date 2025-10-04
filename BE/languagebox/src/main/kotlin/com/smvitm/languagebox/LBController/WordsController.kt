package com.smvitm.languagebox.LBController

import com.smvitm.languagebox.LBService.WordsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("words")
class WordsController(private val wordsService: WordsService) {

    @GetMapping("/getAll")
    fun getAll(): Any? {
        return wordsService.getAll()
    }

    @GetMapping("/getByLen/{len}")
    fun getWordsByLen(@PathVariable len:Int): Any?{
        return wordsService.getWordsByLen(len = len)
    }

    @GetMapping("getByAlpha/{alpha}")
    fun getWordsByAlpha(@PathVariable alpha: String): Any?{
        return wordsService.getWordsByAlpha(alpha = alpha)
    }

    @GetMapping("get/{len}/{alpha}")
    fun getWordsByLen(@PathVariable len:Int,@PathVariable alpha: String): Any?{
        return wordsService.getWordsByAlphaAndLen(alpha = alpha,len = len)
    }

}