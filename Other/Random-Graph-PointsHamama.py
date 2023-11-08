import mysql.connector
import time
from random import randint

print('''

    ██╗  ██╗ █████╗ ███╗   ███╗ █████╗ ███╗   ███╗ █████╗ 
    ██║  ██║██╔══██╗████╗ ████║██╔══██╗████╗ ████║██╔══██╗
    ███████║███████║██╔████╔██║███████║██╔████╔██║███████║
    ██╔══██║██╔══██║██║╚██╔╝██║██╔══██║██║╚██╔╝██║██╔══██║
    ██║  ██║██║  ██║██║ ╚═╝ ██║██║  ██║██║ ╚═╝ ██║██║  ██║
    ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝
                     -- Please Wait --                                  
    ''')

inputCount = int(input('How many measures do you want to insert for every sensor: '))

try:
    connection = mysql.connector.connect(host='localhost', database='sotz', user='root', password='1313')
    time = str(int(time.time() * 1000 * 1000 // 1000) - 3600000000)
    cursor = connection.cursor()
    arr = [0,0,0,0,0,0]

    for i in range(inputCount):
        for j in range(6):
            random = randint(-10, 10)
            arr[j] += random
            insert_query = "INSERT INTO measures (time, sid, value) VALUES('" + time + "', '" + str(j+1) + "', '" + str(arr[j]) + "')"
            cursor.execute(insert_query)
            connection.commit()
        time = str(int(time) + 3600000) # every 10 hours.
    print(cursor.rowcount, "Record inserted successfully into users table")
    cursor.close()

except mysql.connector.Error as error:
    print("Failed to insert record into users table {}".format(error))

finally:
    if connection.is_connected():
        connection.close()