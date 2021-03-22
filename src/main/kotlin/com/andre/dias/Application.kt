package com.andre.dias

import com.google.gson.Gson
import java.io.File

fun main() {
    File("html").walk().forEach {
        runCatching {
            val raw = it.inputStream().readBytes().toString(Charsets.UTF_8)

            val episodeNames = mutableListOf<String>()
            val episodesIds = mutableListOf<String>()

            val episodesNameRegexFind = "\"0\" aria-label=\"([A-Za-zÀ-ÖØ-öø-ÿ]|\\s|,|\\d)*\"".toRegex().findAll(raw)

            episodesNameRegexFind.iterator().forEach { result ->
                val value = result.value
                    .replace("\"", "")
                    .replace("0 aria-label=", "")

                episodeNames.add(value)
            }

            val episodesIdRegexFind = "22video_id%22:\\d*".toRegex().findAll(raw)

            episodesIdRegexFind.iterator().forEach { result ->
                val value = result.value
                    .replace("22video_id%22:", "")

                episodesIds.add(value)
            }

            val result = episodeNames.zip(episodesIds).map {
                Result(it.second, it.first)
            }

            val gson = Gson()

            println(gson.toJson(result))
        }
    }
}

data class Result(val id: String, val title: String)