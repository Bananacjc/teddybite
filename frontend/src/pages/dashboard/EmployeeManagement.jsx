import React, { useEffect, useState } from 'react';
import { 
  Box, Heading, SimpleGrid, Card, CardBody, Avatar, Text, Badge, HStack, Button,
  Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody, ModalCloseButton,
  FormControl, FormLabel, Input, Select, useDisclosure, useToast
} from '@chakra-ui/react';
import { AddIcon } from '@chakra-ui/icons';
import { getAllEmployees, createEmployee, deleteEmployee } from '../../api/employees';

const POSITION_SALARIES = {
  MANAGER: 4500,
  CASHIER: 3800,
  CHEF: 3500,
  WAITER: 3000,
  CLEANER: 2800
};

const EmployeeManagement = () => {
  const [employees, setEmployees] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();

  // Form State
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    contactNo: '',
    gender: 'MALE',
    position: 'WAITER',
    salary: POSITION_SALARIES['WAITER'],
    dob: '',
    dateJoined: new Date().toISOString().split('T')[0]
  });

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const data = await getAllEmployees();
      setEmployees(data);
    } catch (error) {
      toast({
        title: "Error fetching employees",
        description: error.message,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    
    if (name === 'position') {
      setFormData(prev => ({
        ...prev,
        position: value,
        salary: POSITION_SALARIES[value] || 0
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSubmit = async () => {
    try {
      // Format dates for backend (LocalDateTime expects ISO format roughly)
      const payload = {
        ...formData,
        dob: formData.dob ? new Date(formData.dob).toISOString() : null,
        dateJoined: formData.dateJoined ? new Date(formData.dateJoined).toISOString() : null,
        salary: parseFloat(formData.salary) || 0
      };

      await createEmployee(payload);
      
      toast({
        title: "Employee added successfully",
        status: "success",
        duration: 3000,
      });
      onClose();
      fetchEmployees();
      
      // Reset form
      setFormData({
        name: '',
        email: '',
        contactNo: '',
        gender: 'MALE',
        position: 'WAITER',
        salary: POSITION_SALARIES['WAITER'],
        dob: '',
        dateJoined: new Date().toISOString().split('T')[0]
      });

    } catch (error) {
      toast({
        title: "Failed to add employee",
        description: error.response?.data?.message || error.message,
        status: "error",
        duration: 3000,
      });
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this employee?")) {
      try {
        await deleteEmployee(id);
        toast({ title: "Employee deleted", status: "success" });
        fetchEmployees();
      } catch (error) {
        toast({ title: "Delete failed", status: "error" });
      }
    }
  };

  return (
    <Box>
      <HStack justify="space-between" mb={6}>
        <Heading size="lg" color="brown.900">Employee Management</Heading>
        <Button leftIcon={<AddIcon />} colorScheme="brand" bg="brand.500" color="brown.900" onClick={onOpen}>
          Add Employee
        </Button>
      </HStack>

      {isLoading ? (
        <Text>Loading...</Text>
      ) : (
        <SimpleGrid columns={{ base: 1, md: 2, lg: 3 }} spacing={6}>
          {employees.map((emp) => (
            <Card key={emp.employeeID || emp.id} borderRadius="xl" boxShadow="sm">
              <CardBody>
                <HStack spacing={4} align="start">
                  <Avatar name={emp.name} bg="brown.200" />
                  <Box>
                    <Heading size="sm">{emp.name}</Heading>
                    <Text fontSize="sm" color="gray.500">{emp.position}</Text>
                    <Badge colorScheme="green" mt={2}>Active</Badge>
                  </Box>
                </HStack>
                <Text mt={4} fontSize="sm" color="gray.600">
                  ID: {emp.employeeID || 'N/A'}<br/>
                  Email: {emp.email}<br/>
                  Contact: {emp.contactNo}
                </Text>
                <HStack mt={4}>
                  <Button size="sm" flex={1}>View Profile</Button>
                  <Button 
                    size="sm" 
                    colorScheme="red" 
                    variant="outline" 
                    flex={1}
                    onClick={() => handleDelete(emp.employeeID)}
                  >
                    Delete
                  </Button>
                </HStack>
              </CardBody>
            </Card>
          ))}
        </SimpleGrid>
      )}

      {/* Add Employee Modal */}
      <Modal isOpen={isOpen} onClose={onClose} size="xl">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Add New Employee</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <SimpleGrid columns={2} spacing={4}>
              <FormControl isRequired>
                <FormLabel>Full Name</FormLabel>
                <Input name="name" value={formData.name} onChange={handleInputChange} />
              </FormControl>
              <FormControl isRequired>
                <FormLabel>Email</FormLabel>
                <Input name="email" type="email" value={formData.email} onChange={handleInputChange} />
              </FormControl>
              <FormControl isRequired>
                <FormLabel>Contact No</FormLabel>
                <Input name="contactNo" value={formData.contactNo} onChange={handleInputChange} />
              </FormControl>
              <FormControl isRequired>
                <FormLabel>Gender</FormLabel>
                <Select name="gender" value={formData.gender} onChange={handleInputChange}>
                  <option value="MALE">Male</option>
                  <option value="FEMALE">Female</option>
                  <option value="OTHER">Other</option>
                </Select>
              </FormControl>
              <FormControl isRequired>
                <FormLabel>Position</FormLabel>
                <Select name="position" value={formData.position} onChange={handleInputChange}>
                  <option value="MANAGER">Manager</option>
                  <option value="CASHIER">Cashier</option>
                  <option value="CHEF">Chef</option>
                  <option value="WAITER">Waiter</option>
                  <option value="CLEANER">Cleaner</option>
                </Select>
              </FormControl>
              <FormControl isRequired>
                <FormLabel>Salary (RM)</FormLabel>
                <Input name="salary" type="number" value={formData.salary} isReadOnly bg="gray.100" />
              </FormControl>
              <FormControl>
                <FormLabel>Date of Birth</FormLabel>
                <Input name="dob" type="date" value={formData.dob} onChange={handleInputChange} />
              </FormControl>
              <FormControl>
                <FormLabel>Date Joined</FormLabel>
                <Input name="dateJoined" type="date" value={formData.dateJoined} onChange={handleInputChange} />
              </FormControl>
            </SimpleGrid>
          </ModalBody>

          <ModalFooter>
            <Button variant="ghost" mr={3} onClick={onClose}>Cancel</Button>
            <Button colorScheme="brand" bg="brand.500" color="brown.900" onClick={handleSubmit}>
              Save Employee
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Box>
  );
};

export default EmployeeManagement;
