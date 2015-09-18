# parking_lot
Parking Lot Management System

- Requires Java 8. <br/>
- Parking lot capacity can be configured in application-context.xml, default value is 3. <br/>
- Multi-threaded test can be run from com.parking.service.impl.IntegrationTest#multiThreadTestWithRetry(). <br />
The test method simulates 20 cars trying to enter parking lot at the same time. If space is available, will wait randomly 1~10 seconds before exit. <br />
If parking lot is full, will put thread to sleep and create an Observer to wait for parking space to become available,
at which point the car will try to enter again and repeat the process. <br />
