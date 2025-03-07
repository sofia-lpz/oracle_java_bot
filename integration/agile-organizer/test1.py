import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from webdriver_manager.firefox import GeckoDriverManager
from selenium.webdriver.firefox.service import Service as FirefoxService
from selenium.webdriver.firefox.options import Options

class GoogleTestCase(unittest.TestCase):

    def setUp(self):
        options = Options()
        options.headless = True
        self.browser = webdriver.Firefox(service=FirefoxService(GeckoDriverManager().install()) , options=options)
        self.addCleanup(self.browser.quit)

    def test_page_title(self):
        self.browser.get('http://www.google.com')
        self.assertIn('Google', self.browser.title)

    def test_img(self):
        self.browser.get('http://www.google.com')
        someVar = self.browser.find_element(By.XPATH, "/html/body/div[1]/div[2]/div/img")
        print(someVar)

if __name__ == '__main__':
    unittest.main(verbosity=2)
