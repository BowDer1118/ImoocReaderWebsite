create table book
(
    book_id             bigint auto_increment comment '圖書編號'
        primary key,
    book_name           varchar(64)               not null comment '書名',
    sub_title           varchar(128)              not null comment '子標題',
    author              varchar(32)               not null comment '作者',
    cover               varchar(255)              not null comment '封面圖片URL',
    description         text                      not null comment '圖書詳情',
    category_id         bigint                    not null comment '分類編號',
    evaluation_score    float(255, 1) default 0.0 not null comment '圖書評分',
    evaluation_quantity int           default 0   not null comment '評價數量'
)
    charset = utf8mb4;

create table category
(
    category_id   bigint      not null comment '分類編號'
        primary key,
    category_name varchar(32) not null comment '圖書分類'
)
    charset = utf8mb4;

create table evaluation
(
    evaluation_id  bigint auto_increment comment '評價編號'
        primary key,
    content        varchar(255)                 not null comment '短評內容',
    score          int                          not null comment '評分-5分制',
    create_time    datetime                     not null comment '創建時間',
    member_id      bigint                       not null comment '會員編號',
    book_id        bigint                       not null comment '圖書編號',
    enjoy          int         default 0        not null comment '點贊數量',
    state          varchar(16) default 'enable' not null comment '審核狀態 enable-有效 disable-已禁用',
    disable_reason varchar(255)                 null comment '禁用理由',
    disable_time   datetime                     null comment '禁用時間'
)
    charset = utf8mb4;

create table member
(
    member_id   bigint auto_increment comment '會員編號'
        primary key,
    username    varchar(16) not null comment '用戶名',
    password    varchar(64) not null comment '密碼',
    salt        int         not null comment '鹽值',
    create_time datetime    not null comment '創建時間',
    nickname    varchar(16) not null comment '昵稱'
)
    charset = utf8mb4;

create table member_read_state
(
    rs_id       bigint auto_increment comment '會員閱讀狀態'
        primary key,
    book_id     bigint   not null comment '圖書編號',
    member_id   bigint   not null comment '會員編號',
    read_state  int      not null comment '閱讀狀態 1-想看 2-看過',
    create_time datetime not null comment '創建時間'
)
    charset = utf8mb4;

create table test
(
    id      int auto_increment
        primary key,
    content varchar(32) not null
)
    charset = utf8mb4;

create table user
(
    user_id  bigint auto_increment comment '用戶編號'
        primary key,
    username varchar(32) not null comment '用戶名',
    password varchar(64) not null comment '密碼',
    salt     int         not null comment '鹽值'
)
    charset = utf8mb4;

