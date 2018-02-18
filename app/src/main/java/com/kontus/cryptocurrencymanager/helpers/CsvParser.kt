package com.kontus.cryptocurrencymanager.helpers

import com.opencsv.CSVReaderBuilder
import java.io.*
import com.opencsv.CSVParserBuilder

class CsvParser {

    object CSVMetadata {
        var fileLines: MutableList<Array<String>>? = null
        var rows: Int? = null
        var columns: Int? = null
    }

    fun importCSV(file: File): CSVMetadata {
        val fis: FileInputStream = file.inputStream()
        val stringWithSpecialCharacters = getFileContent(fis, "UTF-8")
        val regex = Regex("[^\u0009\u000a\u000d\u0020-\uD7FF\uE000-\uFFFD]")
        var fileString = regex.replace(stringWithSpecialCharacters, "")
        val bais: ByteArrayInputStream = fileString.byteInputStream()
        val br = bais.bufferedReader()
        val csvParser = CSVParserBuilder().withSeparator(',').build()
        val csvReader = CSVReaderBuilder(br).withCSVParser(csvParser).build()

        var list = mutableListOf<Array<String>>()
        var columns = 0

        try {
            while (true) {
                val next = csvReader.readNext()
                if (next != null) {
                    list.add(next)
                    columns = next.size
                } else {
                    break
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fis.close()
            bais.close()
            br.close()
            csvReader.close()
        }

        CSVMetadata.rows = list.size
        CSVMetadata.columns = columns
        CSVMetadata.fileLines = list

        // TODO remove this later
        println(stringWithSpecialCharacters)
        println(fileString)
        list.forEach { record ->
            println("OrderUuid: " + record[0])
            println("Exchange: " + record[1])
            println("Type: " + record[2])
            println("Quantity: " + record[3])
            println("Limit: " + record[4])
            println("CommissionPaid: " + record[5])
            println("Price: " + record[6])
            println("Opened: " + record[7])
            println("Closed: " + record[8])
            println("----------------------------")
        }

        return CSVMetadata
    }

    private fun getFileContent(fis: FileInputStream, encoding: String): String {
        val sb = StringBuilder()
        val isr = InputStreamReader(fis, encoding)
        val buf = CharArray(1024)
        var len = isr.read(buf)
        while (len > 0) {
            sb.append(buf, 0, len)
            len = isr.read(buf)
        }

        return sb.toString()
    }

}