package com.smvitm.languagebox.LBService

import org.springframework.stereotype.Service
import java.io.File

@Service
class WordsService {
    private val f1 : File = File("wordsEng.txt")
    fun getAll(): Any? {
        return try {
            f1.readLines().map { it.trim() }
        }
        catch (e:Exception){
            e.message
        }
    }

    fun getWordsByLen(len:Int): Any? {
        return try {
            f1.readLines().map { it.trim() }.filter { it.length==len }
        }
        catch (e:Exception){
            e.message
        }
    }

    fun getWordsByAlphaAndLen(len:Int,alpha: String):Any?{
        return try{
            f1.readLines().map { it.trim() }.filter { it.length==len && it.startsWith(alpha) }
        }
        catch (e:Exception){
            e.message
        }
    }

    fun getWordsByAlpha(alpha: String):Any?{
        return try{
            f1.readLines().map { it.trim() }.filter { it.startsWith(alpha) }
        }
        catch (e:Exception){
            e.message
        }
    }
}
