package com.lovely.deer.util.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.util.*

/** Media Type */
const val TAG_RECORD = "RECORD"
const val TYPE_IMAGE = "image"
const val TYPE_AUDIO = "audio"
const val TYPE_VIDEO = "video"
const val IMAGE_MIME_TYPE: String = "image/*"
const val AUDIO_MIME_TYPE: String = "audio/*"
const val VIDEO_MIME_TYPE: String = "video/*"
const val FILE_MIME_TYPE: String = "file/*"
const val TXT_MIME_TYPE: String = "text/*"
const val APPLICATION_MSWORD = "application/msword"
const val APPLICATION_XLSX = "application/vnd.ms-excel"
const val APPLICATION_PPTX = "application/vnd.ms-powerpoint"
const val APPLICATION_HWP = "application/haansofthwp"
const val APPLICATION_PDF = "application/pdf"

/** Constant File Type */
const val MP3 = "mp3"
const val MP4 = "mp4"
const val JPG = "jpg"
const val JPEG = "jpeg"
const val GIF = "gif"
const val PNG = "png"
const val BMP = "bmp"
const val TXT = "txt"
const val DOC = "doc"
const val DOCX = "docx"
const val XLS = "xls"
const val XLSX = "xlsx"
const val PPT = "ppt"
const val PPTX = "pptx"
const val HWP = "hwp"
const val PDF = "pdf"

@Parcelize
open class MediaStoreItem(
    open val id: Long,
    open val dateTaken: Date?,
    open val displayName: String?,
    open var contentUri: String?,
    open val type: MediaStoreFileType
):Parcelable

@Parcelize
data class MediaStoreImage(
    override var id: Long,
    override var dateTaken: Date?,
    override var displayName: String?,
    override var contentUri: String?,
    override var type: MediaStoreFileType
) : MediaStoreItem(id, dateTaken, displayName, contentUri, type),Parcelable

@Parcelize
data class MediaStoreVideo(
    override var id: Long,
    override var dateTaken: Date?,
    override var displayName: String?,
    override var contentUri: String?,
    override var type: MediaStoreFileType,
    var _duration: String?
) : MediaStoreItem(id, dateTaken, displayName, contentUri, type),Parcelable

@Parcelize
data class MediaStoreFile(
    override var id: Long,
    override var dateTaken: Date?,
    override var displayName: String?,
    override var contentUri: String?,
    override var type: MediaStoreFileType
) : MediaStoreItem(id, dateTaken, displayName, contentUri, type),Parcelable


data class MediaStoreAudio(
    override var id: Long,
    override var dateTaken: Date?,
    override var displayName: String?,
    override var contentUri: String?,
    override var type: MediaStoreFileType,
    var album: String?,
    var title: String?,
    var _duration: String?
) : MediaStoreItem(id, dateTaken, displayName, contentUri, type)

enum class MediaStoreFileType(
    var externalContentUri: Uri,
    val mimeType: String,
    val directory: String,
    val typeCode: Int
) {
    IMAGE(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        IMAGE_MIME_TYPE,
        Environment.DIRECTORY_PICTURES,
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
    ),
    AUDIO(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        AUDIO_MIME_TYPE,
        Environment.DIRECTORY_MUSIC,
        MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO
    ),
    VIDEO(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        VIDEO_MIME_TYPE,
        Environment.DIRECTORY_MOVIES,
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
    ),
    FILE(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        FILE_MIME_TYPE,
        Environment.DIRECTORY_DOCUMENTS,
        MediaStore.Files.FileColumns.MEDIA_TYPE_NONE
    )
}

fun getExtension(fileStr: String): String {
    val name = fileStr.substringBeforeLast('.')
    val extension = fileStr.substringAfterLast('.')
    return if (name == extension) ""
    else extension
}

fun Context.viewFile(filePath: Uri?, fileName: String?) {
    val fileLinkIntent = Intent(Intent.ACTION_VIEW).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
    }
    val file = File(filePath.toString(), fileName)
    val fileExtend = getExtension(file.absolutePath)
    if (fileExtend.equals(MP3, ignoreCase = true)) {
        fileLinkIntent.setDataAndType(filePath, AUDIO_MIME_TYPE)
    } else if (fileExtend.equals(MP4, ignoreCase = true)) {
        fileLinkIntent.setDataAndType(filePath, VIDEO_MIME_TYPE)
    } else if (fileExtend.equals(JPG, ignoreCase = true)
        || fileExtend.equals(JPEG, ignoreCase = true)
        || fileExtend.equals(GIF, ignoreCase = true)
        || fileExtend.equals(PNG, ignoreCase = true)
        || fileExtend.equals(BMP, ignoreCase = true)
    ) {
        fileLinkIntent.setDataAndType(filePath, IMAGE_MIME_TYPE)
    } else if (fileExtend.equals(TXT, ignoreCase = true)) {
        fileLinkIntent.setDataAndType(filePath, TXT_MIME_TYPE)
    } else if (fileExtend.equals(DOC, ignoreCase = true) || fileExtend.equals(
            DOCX,
            ignoreCase = true
        )
    ) {
        fileLinkIntent.setDataAndType(filePath, APPLICATION_MSWORD)
    } else if (fileExtend.equals(XLS, ignoreCase = true) || fileExtend.equals(
            XLSX,
            ignoreCase = true
        )
    ) {
        fileLinkIntent.setDataAndType(filePath, APPLICATION_XLSX)
    } else if (fileExtend.equals(PPT, ignoreCase = true) || fileExtend.equals(
            PPTX,
            ignoreCase = true
        )
    ) {
        fileLinkIntent.setDataAndType(filePath, APPLICATION_PPTX)
    } else if (fileExtend.equals(PDF, ignoreCase = true)) {
        fileLinkIntent.setDataAndType(filePath, APPLICATION_PDF)
    } else if (fileExtend.equals(HWP, ignoreCase = true)) {
        fileLinkIntent.setDataAndType(filePath, APPLICATION_HWP)
    }
    val pm: PackageManager = this.packageManager
    val list = pm.queryIntentActivities(
        fileLinkIntent,
        PackageManager.GET_META_DATA
    )
    if (list.size == 0) {
        Toast.makeText(this, "$fileName need an app that can run.", Toast.LENGTH_SHORT).show()
    } else {
        this.startActivity(fileLinkIntent.apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        })
    }
}


fun Uri.delete(contentResolver: ContentResolver?) {
    contentResolver?.delete(this, null, null)
}

fun Context.createMediaFile(dirId: String, mediaItem: MediaStoreItem?): File {
    val dirPath = File(externalCacheDir, dirId)
    if (!dirPath.exists()) {
        dirPath.mkdirs()
    }
    val file = File(Uri.parse(mediaItem?.contentUri).path)
    var fileName = file.name.substringBeforeLast('.')
    var fileExtend = getExtension(file.absolutePath)
    if (fileExtend.isEmpty()) {
        fileName = mediaItem?.displayName?.substringBeforeLast('.').toString()
        fileExtend = when (mediaItem?.type) {
            MediaStoreFileType.IMAGE -> PNG
            MediaStoreFileType.AUDIO -> MP3
            MediaStoreFileType.VIDEO -> MP4
            else -> {
                mediaItem?.displayName?.substringAfterLast('.').toString()
            }
        }
    }
    return File(dirPath, "${fileName}.${fileExtend}")
}

fun Context.clearCacheData() {
    val cache = externalCacheDir
    if (cache != null) {
        if (cache.isDirectory) {
            val children: Array<String> = cache.list()
            for (i in children.indices) {
                File(cache, children[i]).delete()
            }
        }
    }
}

fun Context.isFileExist(dirId: String, fName: String): Boolean {
    val dirPath = File(getExternalFilesDir(null), dirId)
    val childList = dirPath.listFiles()
    if (dirPath.exists()) {
        for (file in childList) {
            if (file.name == fName)
                return true
        }
    }
    return false
}

fun Context.isContentUriAvailable(uri: Uri?): Boolean {
    var cursor: Cursor? = null
    try {
        cursor = contentResolver.query(
            uri!!, null, null, null,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            return true
        }
    } finally {
        cursor?.close()
    }
    return false
}

fun getPath(context: Context, uri: Uri): String? {
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    val scheme = uri.scheme
    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                split[1]
            )
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        return getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        Timber.d("getPath, context: $context, uri: $uri")
        return uri.path
    }
    return uri.path // scheme이 null 일 경우 storage.
}


fun Context.getPath(string: String?): String? {
    return getPath(this, Uri.parse(string))
}


/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context The context.
 * @param uri The Uri to query.
 * @param selection (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
fun getDataColumn(
    context: Context, uri: Uri?, selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        cursor = context.contentResolver.query(
            uri!!, projection, selection, selectionArgs,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}


fun Context.getDataFromContentUri(uri: Uri): MediaStoreItem? {
    val type = contentResolver.getMediaItemType(uri)             // 아이템 타입.
    val projection = arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME)
    var query = contentResolver.query(
        uri,
        projection,
        null, // selection
        null, //selectionArgs
        null
    )
    when (type) {
        MediaStoreFileType.IMAGE -> {
            query?.use { cursor ->
                val displayNameColumn = cursor.getColumnIndexOrThrow(projection[0])
                if (cursor.moveToFirst()) {
                    val id = System.currentTimeMillis()
                    val dateModified = Date()
                    val displayName = cursor.getString(displayNameColumn)
                    return MediaStoreImage(
                        id,
                        dateModified,
                        displayName,
                        uri.toString(),
                        MediaStoreFileType.IMAGE
                    )
                }
            }
        }
        MediaStoreFileType.AUDIO -> {
            query?.use { cursor ->
                val displayNameColumn = cursor.getColumnIndexOrThrow(projection[0])
                if (cursor.moveToFirst()) {
                    val id = System.currentTimeMillis()
                    val dateModified = Date()
                    val displayName = cursor.getString(displayNameColumn)
                    var album: String? = getMediaMetaData(uri,
                        MediaMetadataRetriever.METADATA_KEY_ALBUM
                    ) ?: "LovelyNote"
                    var title: String? = getMediaMetaData(uri,
                        MediaMetadataRetriever.METADATA_KEY_TITLE
                    )
                        ?: displayName.substringBeforeLast(".")
                    val duration = getMediaMetaData(uri,
                        MediaMetadataRetriever.METADATA_KEY_DURATION
                    )
                    return MediaStoreAudio(
                        id = id,
                        dateTaken = dateModified,
                        displayName = displayName,
                        album = album,
                        title = title,
                        _duration = duration,
                        contentUri = uri.toString(),
                        type = MediaStoreFileType.AUDIO
                    )
                }
            }
        }
        MediaStoreFileType.VIDEO -> {
            // 외부 비디오 앱을 사용했을 경우.
            if (query == null) {
                return MediaStoreVideo(
                    System.currentTimeMillis(),
                    Date(),
                    uri.toString().substringBeforeLast("."),
                    uri.toString(),
                    MediaStoreFileType.VIDEO,
                    getMediaMetaData(uri, MediaMetadataRetriever.METADATA_KEY_DURATION)
                )
            }
            query.use { cursor ->
                val displayNameColumn = cursor.getColumnIndexOrThrow(projection[0])
                if (cursor.moveToFirst()) {
                    val id = System.currentTimeMillis()
                    val dateModified = Date()
                    val displayName = cursor.getString(displayNameColumn)
                    val duration = getMediaMetaData(uri,
                        MediaMetadataRetriever.METADATA_KEY_DURATION
                    )
                    return MediaStoreVideo(
                        id,
                        dateModified,
                        displayName,
                        uri.toString(),
                        MediaStoreFileType.VIDEO,
                        duration
                    )
                }
            }
        }
        else -> {
            query?.use { cursor ->
                val displayNameColumn = cursor.getColumnIndexOrThrow(projection[0])
                if (cursor.moveToFirst()) {
                    val id = System.currentTimeMillis()
                    val dateModified = Date()
                    val displayName = cursor.getString(displayNameColumn)
                    return MediaStoreFile(
                        id,
                        dateModified,
                        displayName,
                        uri.toString(),
                        MediaStoreFileType.FILE
                    )
                }
            }
        }
    }
    query?.close()
    return null
}

fun ContentResolver.getMediaItemType(uri: Uri): MediaStoreFileType {
    val type = getType(uri)?.run {
        val idx = this.indexOf("/")
        this.substring(0, idx)
    }.apply {
        if (this == null) {
            // content 가 아닐 경우.
            return when (uri.toString().substringAfterLast(".")) {
                MP3 -> {
                    MediaStoreFileType.AUDIO
                }
                MP4 -> {
                    MediaStoreFileType.VIDEO
                }
                JPG, JPEG, PNG, BMP, GIF -> {
                    MediaStoreFileType.IMAGE
                }
                else -> {
                    MediaStoreFileType.FILE
                }
            }
        }
    }
    return when (type) {
        TYPE_IMAGE -> MediaStoreFileType.IMAGE
        TYPE_VIDEO -> MediaStoreFileType.VIDEO
        TYPE_AUDIO -> MediaStoreFileType.AUDIO
        else -> MediaStoreFileType.FILE
    }
}

fun Context.getMediaMetaData(uri: Uri, metaData: Int): String? {
    val retriever = MediaMetadataRetriever();
    var data:String?=null
    try {
        retriever.setDataSource(this, uri);
        data = retriever.extractMetadata(metaData);
        retriever.release()
    }catch (e:Exception){
        return null
    }
    return when (metaData) {
        MediaMetadataRetriever.METADATA_KEY_DURATION -> data?.toLong().toString()
        MediaMetadataRetriever.METADATA_KEY_ALBUM, MediaMetadataRetriever.METADATA_KEY_TITLE -> data
        else -> null
    }
}