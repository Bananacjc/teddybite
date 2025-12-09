import React, { useEffect, useState } from 'react';
import {
  Box, Heading, SimpleGrid, Card, CardBody, Avatar, Text, Badge, HStack, Button,
  Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody, ModalCloseButton,
  FormControl, FormLabel, Input, Select, useDisclosure, useToast,
  FormErrorMessage, InputGroup, InputRightElement, IconButton
} from '@chakra-ui/react';
import { AddIcon, ViewIcon, ViewOffIcon } from '@chakra-ui/icons';
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

  const [errors, setErrors] = useState({});

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  // Form State
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    contactNo: '',
    gender: 'MALE',
    position: 'WAITER',
    salary: POSITION_SALARIES['WAITER'],
    dob: '',
    password: '',
    confirmPassword: ''
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

    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleSubmit = async () => {
    setErrors({});

    // 2. Validate Password Match
    if (formData.password !== formData.confirmPassword) {
      setErrors({ confirmPassword: "Password do not match" });
      return;
    }

    try {
      // Format dates for backend
      const payload = {
        name: formData.name,
        email: formData.email,
        contactNo: formData.contactNo,
        gender: formData.gender,
        position: formData.position,
        password: formData.password, // Send only the password
        dob: formData.dob ? `${formData.dob}T00:00:00` : null,
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
        dob: '',
        password: '',
        confirmPassword: ''
      });
      setShowPassword(false);
      setShowConfirmPassword(false);

    } catch (error) {
      console.log("FULL ERROR RESPONSE:", error.response);
      if (error.response && error.response.status === 400) {
        console.log("DATA RECEIVED:", error.response.data); // <--- Add this
        setErrors(error.response.data);
      } else {
        toast({
          title: "Failed to add employee",
          //description: error.response?.data?.message || error.message,
          description: "Something went wrong",
          status: "error",
          duration: 3000,
        });
      }
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
                  ID: {emp.employeeID || 'N/A'}<br />
                  Email: {emp.email}<br />
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
              {/* NAME FIELD */}
              <FormControl isRequired isInvalid={!!errors.name}>
                <FormLabel>Full Name</FormLabel>
                <Input name="name" value={formData.name} onChange={handleInputChange} />
                <FormErrorMessage>{errors.name}</FormErrorMessage>
              </FormControl>

              {/* EMAIL FIELD */}
              <FormControl isRequired isInvalid={!!errors.email}>
                <FormLabel>Email</FormLabel>
                <Input name="email" type="email" value={formData.email} onChange={handleInputChange} />
                <FormErrorMessage>{errors.email}</FormErrorMessage>
              </FormControl>

              {/* PASSWORD FIELD */}
              <FormControl isRequired isInvalid={!!errors.password}>
                <FormLabel>Password</FormLabel>
                <InputGroup>
                  <Input
                    name="password"
                    type={showPassword ? "text" : "password"}
                    placeholder="Min 6 chars"
                    value={formData.password}
                    onChange={handleInputChange}
                  />
                  <InputRightElement>
                    <IconButton
                      variant="ghost"
                      icon={showPassword ? <ViewOffIcon /> : <ViewIcon />}
                      onClick={() => setShowPassword(!showPassword)}
                      aria-label={showPassword ? "Hide password" : "Show password"}
                      size="sm"
                    />
                  </InputRightElement>
                </InputGroup>
                <FormErrorMessage>{errors.password}</FormErrorMessage>
              </FormControl>

              {/* CONFIRM PASSWORD FIELD */}
              <FormControl isRequired isInvalid={!!errors.confirmPassword}>
                <FormLabel>Confirm Password</FormLabel>
                <InputGroup>
                  <Input
                    name='confirmPassword'
                    type={showConfirmPassword ? "text" : "password"}
                    placeholder='Re-enter password'
                    value={formData.confirmPassword}
                    onChange={handleInputChange}
                  />
                  <InputRightElement>
                    <IconButton
                      variant="ghost"
                      icon={showConfirmPassword ? <ViewOffIcon /> : <ViewIcon />}
                      onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                      aria-label={showConfirmPassword ? "Hide password" : "Show password"}
                      size="sm"
                    />
                  </InputRightElement>
                </InputGroup>

                <FormErrorMessage>{errors.confirmPassword}</FormErrorMessage>
              </FormControl>

              {/* CONTACT NUMBER FIELD */}
              <FormControl isRequired isInvalid={!!errors.contactNo}>
                <FormLabel>Contact No</FormLabel>
                <Input name="contactNo" value={formData.contactNo} onChange={handleInputChange} />
                <FormErrorMessage>{errors.contactNo}</FormErrorMessage>
              </FormControl>

              {/* GENDER FIELD */}
              <FormControl isRequired isInvalid={!!errors.gender}>
                <FormLabel>Gender</FormLabel>
                <Select name="gender" value={formData.gender} onChange={handleInputChange}>
                  <option value="MALE">Male</option>
                  <option value="FEMALE">Female</option>
                  <option value="OTHER">Other</option>
                </Select>
                <FormErrorMessage>{errors.gender}</FormErrorMessage>
              </FormControl>

              {/* POSITION FIELD */}
              <FormControl isRequired isInvalid={!!errors.position}>
                <FormLabel>Position</FormLabel>
                <Select name="position" value={formData.position} onChange={handleInputChange}>
                  <option value="MANAGER">Manager</option>
                  <option value="CASHIER">Cashier</option>
                  <option value="CHEF">Chef</option>
                  <option value="WAITER">Waiter</option>
                  <option value="CLEANER">Cleaner</option>
                </Select>
                <FormErrorMessage>{errors.position}</FormErrorMessage>
              </FormControl>

              <FormControl isRequired isInvalid={!!errors.DOB}>
                <FormLabel>Date of Birth</FormLabel>
                <Input name="dob" type="date" value={formData.dob} onChange={handleInputChange} />
                <FormErrorMessage>{errors.DOB}</FormErrorMessage>
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
