package com.kontus.cryptocurrencymanager.helpers

import com.opencsv.CSVReader
import java.io.*

class CsvParser {

    fun importCSV(file: File): MutableList<Array<String>> {

//        // ex 1
//        val list = mutableListOf<String>()
//        file.useLines { lines -> list.addAll(lines) }
//        list.forEachIndexed { i, line -> println("${i}: " + line) }
//        list.forEach { println(it) }


        // ex 2
//        val csvReader = CSVReader(FileReader(file.path))
//        val list = csvReader.readAll()
//
//        for (record in list) {
//            println("Name : " + record[0])
//            println("Class : " + record[1])
//            println("Dorm : " + record[2])
//            println("Room : " + record[3])
//            println("GPA : " + record[4])
//            println("---------------------------")
//        }


//        // ex 3
//        var list = mutableListOf<Array<String>>()
//        val csvReader = CSVReader(FileReader(file.path))
//        try {
//            while (true) {
//                val next = csvReader.readNext()
//                if (next != null) {
//                    list.add(next)
//                } else {
//                    break
//                }
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            csvReader.close()
//        }
//
//        list.forEach { record ->
//            println("Name : " + record[0])
//            println("Class : " + record[1])
//            println("Dorm : " + record[2])
//            println("Room : " + record[3])
//            println("GPA : " + record[4])
//            println("---------------------------")
//        }


//        // ex 4
//        println(file.readText(charset = Charsets.UTF_8))


//        // ex 5
//        val bufferedReader = file.bufferedReader()
//        val text: List<String> = bufferedReader.readLines()
//        for(line in text){
//            println(line)
//        }


//        // ex 6
//        val inputStream: InputStream = FileInputStream(file)
//        val list = mutableListOf<String>()
//        inputStream.bufferedReader().useLines { lines -> lines.forEach { list.add(it)} }
//        list.forEach{println(it)}


//        // ex 7
//        var list = mutableListOf<Array<String>>()
//
//        val inputStream: InputStream = file.inputStream()
//        val bufferedReader = inputStream.bufferedReader()
//        val csvReader = CSVReader(bufferedReader)
//
//        try {
//            while (true) {
//                val next = csvReader.readNext()
//                if (next != null) {
//                    list.add(next)
//                } else {
//                    break
//                }
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            csvReader.close()
//        }
//
//        list.forEach { record ->
//            println("Name : " + record[0])
//            println("Class : " + record[1])
//            println("Dorm : " + record[2])
//            println("Room : " + record[3])
//            println("GPA : " + record[4])
//
//            println("GPA : " + record[5])
//            println("GPA : " + record[6])
//            println("GPA : " + record[7])
//            println("GPA1 : " + record[8])
//            println("---------------------------")
//        }


        // ex 8
        var list = mutableListOf<Array<String>>()

        val inputStream: FileInputStream = file.inputStream()
        val strWithSpecialCharacters = getFileContent(inputStream, "UTF-8")
        val regex = Regex("[^\u0009\u000a\u000d\u0020-\uD7FF\uE000-\uFFFD]")
        var fileString = regex.replace(strWithSpecialCharacters, "")

        println(strWithSpecialCharacters)
        println(fileString)

        val bais: ByteArrayInputStream = fileString.byteInputStream()
        val bufferedReader = bais.bufferedReader()
        val csvReader = CSVReader(bufferedReader)

        try {
            while (true) {
                val next = csvReader.readNext()
                if (next != null) {
                    list.add(next)
                } else {
                    break
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            csvReader.close()
        }

        list.forEach { record ->
            println("Name : " + record[0])
            println("Class : " + record[1])
            println("Dorm : " + record[2])
            println("Room : " + record[3])
            println("GPA : " + record[4])

            println("GPA : " + record[5])
            println("GPA : " + record[6])
            println("GPA : " + record[7])
            println("GPA1 : " + record[8])
            println("---------------------------")
        }

        return list
    }

    private fun getFileContent(fis: FileInputStream, encoding: String): String {
        val sb = StringBuilder()
        val r = InputStreamReader(fis, encoding)
        val buf = CharArray(1024)
        var amt = r.read(buf)
        while (amt > 0) {
            sb.append(buf, 0, amt)
            amt = r.read(buf)
        }

        return sb.toString()
    }

}