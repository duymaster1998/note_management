package com.example.note_management.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.note_management.models.NoteModel

class NoteDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "notes.db"
        private val TABLE_NAME = "noteTable"
        private val COLUMN_ID = "id"
        private val COLUMN_NOTE = "note"
    }

    private val CREATE_TABLE = (
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NOTE TEXT)")

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNote(note: NoteModel): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTE, note.note)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    val allNotes: List<NoteModel>
        @SuppressLint("Recycle")
        get() {
            val notes = mutableListOf<NoteModel>()
            val query = "SELECT  * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC"
            val db = readableDatabase
            val cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
                val noteModel = NoteModel(id, note)
                notes.add(noteModel)
            }
            cursor.close()
            db.close()
            return notes
        }

    fun getNoteById(id: Long): NoteModel {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $id"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))

        cursor.close()
        db.close()
        return NoteModel(note = note)
    }

    fun getNoteByField(input: String): List<NoteModel> {
        val notes = mutableListOf<NoteModel>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NOTE LIKE ? ORDER BY $COLUMN_ID DESC"
        val selectionArgs = arrayOf("%$input%")
        val cursor = db.rawQuery(query, selectionArgs)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
            val noteModel = NoteModel(id, note)
            notes.add(noteModel)
        }
        cursor.close()
        db.close()
        return notes
    }

    fun updateNote(note: NoteModel) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTE, note.note)
        }

        val where = "$COLUMN_ID = ?"
        val whereArg = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, where, whereArg)
        db.close()
    }
}
