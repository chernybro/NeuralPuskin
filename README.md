# NeuralPushkin
Приложение для удобного взаимодействия с API, которое генерирует тексты на основе полученного текста. Нейросеть обучалась по текстам Александра Сергеевича Пушкина.
Когда бэкенд будет дописан, в приложение появиться возможность добавлять факты о собеседнике, и текст будет генерироваться с учетом этих фактов и общего контекста переписки.

# Из чего состоит
* Состоит из двух фрагментов, "*Контакты*" и "*Сообщения*", и двух активностей - "*Добавление пользователя*" и "*Чат*". 

* Для хранения данных используется библиотека **Room**.

* Для работы с сетью используется **Retrofit** и **OkHtpp**.


Скриншоты экранов приложения представлены ниже.
| <img src="materials/1.jpg"> | <img src="materials/2.jpg"> | <img src="materials/3.jpg"> |
| ------------------------------------------- | ------------------------------------------- |------------------------------------------- |
| <img src="materials/4.jpg"> | <img src="materials/5.jpg"> | 

## Идеи для улучшения приложения.

1. Хранить сущности базы данных в *LiveData*, а после используя *RxJava*.
2. Мигрировать весь код на *Kotlin*.
3. Создать класс репозиторий для получения данных в отдельном месте.

## Установка
Чтобы опробовать приложение, установите файл [PushkinApp](https://github.com/chernybro/NeuralPuskin/blob/main/Pushkin.apk) на ваше устройство.
