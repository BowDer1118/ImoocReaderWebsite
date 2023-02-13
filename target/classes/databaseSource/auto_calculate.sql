# 更新book table中，所有book的score與quantity
update book b set
# if null(如果不為空,為空) 依照是不是null來填資料
evaluation_score =(select ifnull(avg(score),0) from evaluation where book_id=b.book_id and state='enable'),
evaluation_quantity=(select ifnull(count(*),0) from evaluation where book_id=b.book_id and state='enable')
