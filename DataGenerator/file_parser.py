import configparser
import os.path as path
import json
import random
import time
import datetime
import csv


class JsonParser:
    global config
    global data_rows_count
    config = configparser.ConfigParser()
    config.read('file_locations/config.ini')
    data_rows_count = config['size']['data_rows']

    def generate_csv(self):
        file_location = {}
        with open('file_locations/json_file.py') as json_files_location:
            exec(json_files_location.read(), file_location)
            json_files = file_location['__files__']
            tenants = self.get_tenants()
            json_files_location.close()
            for tenant in tenants:
                json_file = json_files + "/" + tenant + ".json"
                if path.exists(json_file):
                    tenant_name, csv_channels_list = self.parse_file(json_file)
                    self.create_csv(tenant_name, csv_channels_list)

    @staticmethod
    def get_tenants():
        tenants_string = config['tenants']['name']
        tenants = str.split(tenants_string, ',')
        return tenants

    def parse_file(self, json_file):
        with open(json_file, 'r') as file:
            tenant_json_file = json.loads(file.read())
            tenant_name = tenant_json_file['tenant']['name']
            channel_names = tenant_json_file['tenant']['channels']['channel']
            csv_channels_list = []
            for channel in channel_names:
                channel_name = channel['name']
                parameters = channel['parameters']
                csv_values_dict = {}
                for parameter in parameters:
                    parameter_name = parameter['parameter_name']
                    parameter_type = parameter['parameter_type']
                    if parameter_type == 'String':
                        random_values = self.get_random_item(parameter['values'])
                    elif parameter_type == 'integer':
                        random_values = self.generate_random_value(parameter['minimum'], parameter['maximum'], 'i')
                        csv_values_dict[parameter_name] = random_values
                    elif parameter_type == 'number':
                        random_values = self.generate_random_value(parameter['minimum'], parameter['maximum'], 'n')
                        csv_values_dict[parameter_name] = random_values
                    else:
                        raise Exception('Undefined parameter type.')
                    csv_values_dict[parameter_name] = random_values

                dm_chane1 = DigitalMediaChannel()
                dm_chane1.channel_name = channel_name
                dm_chane1.channel_values = csv_values_dict
                csv_channels_list.append(dm_chane1)
        file.close()
        return tenant_name, csv_channels_list

    @staticmethod
    def get_random_item(values):
        selected_values = []
        for i in range(int(data_rows_count)):
            index = random.randint(1, len(values))
            selected_values.append(values[index - 1])
        return selected_values

    @staticmethod
    def generate_random_value(minimum, maximum, flag):
        random_values = []
        if flag == 'i':
            for i in range(int(data_rows_count)):
                random_values.append(random.randint(minimum, maximum))
        elif flag == 'n':
            for i in range(int(data_rows_count)):
                random_values.append(round(random.uniform(minimum, maximum), 2))
        return random_values

    @staticmethod
    def create_csv(tenant_name, csv_channels_list):
        destination_location = config['output']['destination']
        time_format = datetime.datetime.fromtimestamp(time.time()).strftime('%Y-%m-%d_%H-%M-%S.%f')
        for i in range(0, len(csv_channels_list)):
            channel_name = csv_channels_list[i].channel_name
            file_path = destination_location + "/" + tenant_name + '_' + channel_name + '_' + time_format + ".csv"
            channel_values = csv_channels_list[i].channel_values
            with open(file_path, "w", newline='') as csv_file:
                csv_writer = csv.writer(csv_file, delimiter=',', quotechar='|', quoting=csv.QUOTE_MINIMAL)
                keys_list = channel_values.keys()
                csv_writer.writerow(keys_list)
                values_list = []
                for index in range(int(data_rows_count)):
                    for key in keys_list:
                        values_list.append(channel_values.get(key)[index])
                    csv_writer.writerow(values_list)
                    values_list.clear()
            csv_file.close()


class DigitalMediaChannel:
    channel_name = ""
    channel_values = {}


jp = JsonParser()
jp.generate_csv()
