# Контроллер

![Я имею контроль](item:tis3d:controller)

Блок контроллера является ядром любого компьютера TIS-3D. Он обеспечивает питание всех подключённых [корпусов](casing.md) и обновляет состояние компьютера TIS-3D. Контроллер возможно питать, используя сигнал красного камня, чем выше будет сила входного сигнала, тем более высокой будет скорость работы. Частный случай: сила входного сигнала равна одному (1) приведёт к тому, что компьютер TIS-3D перейдёт в приостановленное состояние, т. е. его состояние не будет обновляться, но он также не будет выключен и, следовательно, сброшен.

Один блок контроллера может поддерживать до восьми (8) блоков [корпусов](casing.md). Если к контроллеру подключено больше блоков корпусов, контроллер выполнит аварийное выключение и прекратит работу до тех пор, пока количество подключённых [корпусов](casing.md) не будет достаточно уменьшено.

Контроллеры должны быть не связаны друг с другом. Если несколько контроллеров связаны друг с другом, прямо или косвенно через ряд [корпусов](casing.md), все контроллеры будут выполнять аварийное выключение и прекращать работу до тех пор, пока они больше не будут подключены к каким-либо другим контроллерам.

Соединения между контроллером и [корпусом](casing.md) или между [корпусом](casing.md) и другим [корпусом](casing.md) устанавливаются общими сторонами.