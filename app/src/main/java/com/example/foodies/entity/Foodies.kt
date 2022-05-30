package com.example.foodies.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "fav_dishes_table")
data class Foodies (
@ColumnInfo val image:String ,
        @ColumnInfo val imageSource:String,
@ColumnInfo val title:String,
@ColumnInfo val type:String,
@ColumnInfo val category:String,
@ColumnInfo val ingredients:String,
@ColumnInfo(name = "cookingTime") val cooking_time: String,
@ColumnInfo(name = "instructions") val direction_to_cook: String,
@ColumnInfo(name = "favoriteDish") var favorite_dish: Boolean = false,
@PrimaryKey(autoGenerate = true) val id: Int = 0


):Parcelable