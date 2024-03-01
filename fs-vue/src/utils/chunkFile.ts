import SparkMD5 from 'spark-md5';
const DEFAULT_CHUNK_SIZE = 20 * 1024 * 1024;

export const md5File = (file, chunkSize:number = DEFAULT_CHUNK_SIZE) => {

    return new Promise((resolve, reject) => {
        // 开始计算的毫秒值
        const startMs = new Date().getTime();
        let blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;
        // 分片数量
        let chunks = Math.ceil(file.size / chunkSize);
        // 当前分片下标
        let currentChunk = 0;
        // 追加数组缓冲区
        let spark = new SparkMD5.ArrayBuffer();
        // 读取文件
        let fileReader = new FileReader();
        fileReader.onload = function (e) {
            spark.append(e.target.result as ArrayBuffer);
            currentChunk ++;
            if (currentChunk < chunks) {
                loadNext();
            } else {
                const md5 = spark.end(); //完成md5的计算，返回十六进制结果。
                console.log('文件md5计算结束，总耗时：', (new Date().getTime() - startMs) / 1000, 's')
                resolve(md5);
            }
        };
        fileReader.onerror = function (e) {
            reject(e);
        };

        function loadNext() {
            console.log('当前part number：', currentChunk, '总块数：', chunks);
            let start = currentChunk * chunkSize;
            let end = start + chunkSize;
            (end > file.size) && (end = file.size);
            fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
        }
        loadNext();
    })
}