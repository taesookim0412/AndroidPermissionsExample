//AndroidXML:
//<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />


class PermissionsUtil(val context: Context) {
    var songList = mutableListOf<String>()

    fun getAllSongsFromSDCARD() {
        val STAR = arrayOf("*")
        val allsongsuri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val cursor = context.contentResolver.query(allsongsuri, STAR, selection, null, null)


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val song_name: String = cursor
                        .getString(
                            cursor
                                .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                        )
                    val song_id: Int = cursor.getInt(
                        cursor
                            .getColumnIndex(MediaStore.Audio.Media._ID)
                    )
                    val fullpath: String = cursor.getString(
                        cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA)
                    )
                    val album_name: String = cursor.getString(
                        cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM)
                    )
                    val album_id: Int = cursor.getInt(
                        cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    )
                    val artist_name: String = cursor.getString(
                        cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    )
                    val artist_id: Int = cursor.getInt(
                        cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
                    )
                    println("SONG STUFF: ${song_id} ${song_name}")
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }

    fun beginMusicScan(activity: MainActivity) = when {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED -> {
            getAllSongsFromSDCARD()
        }
        else->{
            activity.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0 )
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("${requestCode}, ${grantResults.contentToString()}")
        if (requestCode == 0){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionsUtil.getAllSongsFromSDCARD()
            }
        }
    }
}

