const MILLI = 1;
const SECOND = 1000 * MILLI;
const MINUTE = 60 * SECOND;
const HOUR = 60 * MINUTE;
const DAY = 24 * HOUR;
const MONTH = 30 * DAY;
const YEAR = 12 * MONTH;

function calculateWrittenTime(updateTime) {
    const writtenTime = new Date(Date.now() - new Date(new Date(updateTime).toISOString())).getTime();

    if (writtenTime > YEAR) {
        return Math.floor(writtenTime / YEAR) + "년 전";
    }

    if (writtenTime > MONTH) {
        return Math.floor(writtenTime / MONTH) + "개월 전";
    }

    if (writtenTime > DAY) {
        return Math.floor(writtenTime / DAY) + "일 전";
    }

    if (writtenTime > HOUR) {
        return Math.floor(writtenTime / HOUR) + "시간 전";
    }

    if (writtenTime > MINUTE) {
        return Math.floor(writtenTime / MINUTE) + "분 전";
    }

    return Math.floor(writtenTime / SECOND) + "초 전";
}